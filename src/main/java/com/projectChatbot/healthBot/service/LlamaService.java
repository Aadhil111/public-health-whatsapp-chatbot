package com.projectChatbot.healthBot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class LlamaService {

    private final String API_URL =
            "https://router.huggingface.co/v1/chat/completions";

    private final String HF_TOKEN = System.getenv("HF_TOKEN");

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String askLlama(String prompt) {

        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {

            try {

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(HF_TOKEN);
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, String> message = new HashMap<>();
                message.put("role", "user");
                message.put("content", prompt);

                List<Map<String, String>> messages = List.of(message);

                Map<String, Object> body = new HashMap<>();
                body.put("model", "meta-llama/Llama-3.3-70B-Instruct:novita");
                body.put("messages", messages);
                body.put("temperature", 0.2);

                HttpEntity<Map<String, Object>> entity =
                        new HttpEntity<>(body, headers);

                ResponseEntity<String> response = restTemplate.exchange(
                        API_URL,
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                JsonNode root = objectMapper.readTree(response.getBody());

                return root
                        .path("choices")
                        .get(0)
                        .path("message")
                        .path("content")
                        .asText();

            } catch (HttpClientErrorException.TooManyRequests e) {

                System.out.println("⚠ 429 received. Retrying attempt " + attempt);

                if (attempt == maxAttempts) {
                    System.out.println("❌ Max retry attempts reached.");
                    return null;
                }

                try {
                    Thread.sleep(2500L * attempt); // 1s, 2s, 3s  waiting period between retry.
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            } catch (Exception e) {

                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}