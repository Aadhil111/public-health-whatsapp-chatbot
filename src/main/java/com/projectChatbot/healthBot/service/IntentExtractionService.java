package com.projectChatbot.healthBot.service;

import com.projectChatbot.healthBot.dto.IntentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class IntentExtractionService {

    private final LlamaService llamaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public IntentExtractionService(LlamaService llamaService) {
        this.llamaService = llamaService;
    }

    public IntentResponse extractIntent(String userMessage) {

        String prompt = """
You are a deterministic intent classification engine for a multilingual health chatbot.

Step 1:
Internally translate the user message to English.

Step 2:
Classify intent using ONLY the rules below.

---------------------------------------
INTENT DECISION RULES:

If the message asks about signs, symptoms, effects of a specific disease → SYMPTOMS

If the message asks how to prevent a specific disease → PREVENTION

If the message asks about vaccine of a specific disease → VACCINATION

If the message asks general information about a specific disease → DISEASE_INFO

If user describes symptoms but does NOT mention a disease →
GENERAL_HEALTH

If user asks about chatbot identity, name, abilities, what you can do →
CHAT

If disease not clear and message unclear → UNKNOWN

You MUST choose exactly one of:
SYMPTOMS, PREVENTION, VACCINATION, DISEASE_INFO, GENERAL_HEALTH, CHAT, UNKNOWN

---------------------------------------
LANGUAGE DETECTION:

Choose exactly one:
English, Tamil, Tanglish, Malayalam, Manglish, Telugu

---------------------------------------
CRITICAL RULES:

- Return ONLY valid JSON.
- No explanation.
- No markdown.
- Disease name MUST be in English.
- If no disease mentioned, set disease as null.

---------------------------------------
User message:
"%s"

Return strictly:

{
  "intent": "",
  "disease": "",
  "language": ""
}
""".formatted(userMessage);

        try {
            String response = llamaService.askLlama(prompt);

            System.out.println("LLM RAW RESPONSE FOR INTENT EXTRACTION: " + response);

            if (response == null || response.isBlank()) {
                return fallback();
            }

            response = cleanJson(response);

            return objectMapper.readValue(response, IntentResponse.class);

        } catch (Exception e) {
            return fallback();
        }
    }

    private String cleanJson(String text) {
        return text
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

    private IntentResponse fallback() {
        IntentResponse res = new IntentResponse();
        res.setIntent("UNKNOWN");
        res.setDisease(null);
        res.setLanguage("en");
        return res;
    }
}