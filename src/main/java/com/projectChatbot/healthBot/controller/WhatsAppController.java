package com.projectChatbot.healthBot.controller;

import com.projectChatbot.healthBot.dto.IntentResponse;
import com.projectChatbot.healthBot.entity.User;
import com.projectChatbot.healthBot.repository.UserRepository;
import com.projectChatbot.healthBot.service.IntentExtractionService;
import com.projectChatbot.healthBot.service.DiseaseQueryService;
import com.projectChatbot.healthBot.service.ResponseFormatterService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {

    private final IntentExtractionService intentService;
    private final DiseaseQueryService queryService;
    private final ResponseFormatterService formatterService;
    private final UserRepository userRepository;

    public WhatsAppController(
            IntentExtractionService intentService,
            DiseaseQueryService queryService,
            ResponseFormatterService formatterService,
            UserRepository userRepository
    ) {
        this.intentService = intentService;
        this.queryService = queryService;
        this.formatterService = formatterService;
        this.userRepository = userRepository;
    }

    // ✅ THIS is the webhook endpoint
    @PostMapping(
            value = "/webhook",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String receiveMessage(
            @RequestParam("Body") String body,
            @RequestParam("From") String from
    ) {

        String cleanNumber = from.replace("whatsapp:", "");

        // Efficient user save
        userRepository.findByPhoneNumber(cleanNumber)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setPhoneNumber(cleanNumber);
                    newUser.setSubscribed(true);
                    return userRepository.save(newUser);
                });

        System.out.println("Message from " + from + ": " + body);

        IntentResponse intent = intentService.extractIntent(body);

        String finalReply;

        // ✅ CASE 1: Disease present → DB Flow
        if (intent.getDisease() != null) {

            String rawContent = queryService.fetchContent(intent);

            System.out.println("RAW OUTPUT : " + rawContent);

            finalReply = formatterService.formatResponse(
                    rawContent,
                    intent.getIntent(),
                    intent.getLanguage()
            );
        }

        // ✅ CASE 2: GENERAL HEALTH (Symptom based, no disease)
        else if ("GENERAL_HEALTH".equals(intent.getIntent())) {

            finalReply = formatterService.handleGeneralHealthQuery(
                    body,
                    intent.getLanguage()
            );
        }

        // ✅ CASE 3: CHAT
        else if ("CHAT".equals(intent.getIntent())) {

            finalReply = formatterService.handleChatQuery(
                    body,
                    intent.getLanguage()
            );
        }

        // ✅ CASE 4: UNKNOWN
        else {
            finalReply = formatterService.fallbackMessage(intent.getLanguage());
        }

        System.out.println("FINAL FORMATED OUTPUT :" + finalReply);

        String safeReply = escapeXml(finalReply);

        return """
            <Response>
                <Message>%s</Message>
            </Response>
            """.formatted(safeReply);
    }

    private String escapeXml(String text) {
        if (text == null) return "";
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}