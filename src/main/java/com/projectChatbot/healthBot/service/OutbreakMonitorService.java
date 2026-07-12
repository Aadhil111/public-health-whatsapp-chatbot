package com.projectChatbot.healthBot.service;

import com.projectChatbot.healthBot.entity.TrackedPdf;
import com.projectChatbot.healthBot.repository.TrackedPdfRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OutbreakMonitorService {

    private final TrackedPdfRepository trackedPdfRepository;
    private final WhatsAppSenderService whatsAppSenderService;

    public OutbreakMonitorService(TrackedPdfRepository trackedPdfRepository,
                                  WhatsAppSenderService whatsAppSenderService) {
        this.trackedPdfRepository = trackedPdfRepository;
        this.whatsAppSenderService = whatsAppSenderService;
    }

    @Scheduled(fixedDelay = 6 * 60 * 60 * 1000) // every 6 hours
    public void checkForNewPdfs() {
        checkNCDC();
    }

    private void checkNCDC() {

        String url = "https://ncdc.mohfw.gov.in/cd-alert/";
        fetchWithRetry(url, "NCDC");
    }

    private void fetchWithRetry(String url, String source) {

        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {

            try {

                System.out.println("Connecting to " + source + " (Attempt " + attempt + ")");

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Accept-Language", "en-US,en;q=0.5")
                        .header("Connection", "keep-alive")
                        .timeout(30000)
                        .ignoreHttpErrors(true)
                        .followRedirects(true)
                        .get();

                Elements links = doc.select("a[href$=.pdf]");

                for (Element link : links) {

                    String pdfUrl = link.attr("abs:href");

                    if (!trackedPdfRepository.existsByUrl(pdfUrl)) {

                        trackedPdfRepository.save(new TrackedPdf(pdfUrl, source));
                        sendAlert(source, pdfUrl);
                    }
                }

                System.out.println(source + " check completed successfully.");
                return; // SUCCESS → exit retry loop

            } catch (Exception e) {

                System.out.println(source + " attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == maxRetries) {
                    System.out.println(source + " unreachable after 3 attempts.");
                }

                try {
                    Thread.sleep(5000); // wait 5 seconds before retry
                } catch (InterruptedException ignored) {}
            }
        }
    }

    private void sendAlert(String source, String pdfUrl) {

        String message = "⚠️ New Disease Outbreak Bulletin\n\n" +
                "Source: " + source + "\n" +
                "Download Report:\n" + pdfUrl + "\n\n" +
                "Stay alert. Reply HELP for precautions.";

        whatsAppSenderService.sendToAllUsers(message);
    }
}
