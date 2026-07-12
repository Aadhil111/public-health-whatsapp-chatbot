package com.projectChatbot.healthBot.service;

import org.springframework.stereotype.Service;

@Service
public class ResponseFormatterService {

    private final LlamaService llamaService;

    public ResponseFormatterService(LlamaService llamaService) {
        this.llamaService = llamaService;
    }

    // 🔹 DB Formatting
    public String formatResponse(
            String rawContent,
            String intent,
            String language
    ) {

        if (rawContent == null || rawContent.isBlank()) {
            return fallbackMessage(language);
        }

        String prompt = """
You are a medical information formatter.

Instructions:
- Use ONLY the information provided.
- Do NOT add new medical facts.
- Simplify language.
- Do NOT give prescriptions.
- Keep tone calm.

Reply strictly in %s.

Medical information:
%s
""".formatted(language, rawContent);

        String response = llamaService.askLlama(prompt);

        return (response == null || response.isBlank())
                ? fallbackMessage(language)
                : response.trim();
    }

    // 🔹 GENERAL HEALTH (No diagnosis allowed)
    public String handleGeneralHealthQuery(String userMessage, String language) {

        String prompt = """
You are a public health awareness assistant.

The user is describing symptoms but has not mentioned a specific disease.

IMPORTANT SAFETY RULES:
- Do NOT diagnose a disease.
- Do NOT say "You have X disease".
- Provide general guidance only.
- Encourage consulting a qualified healthcare professional if symptoms are severe.
- Strictly Do NOT prescribe medicines.
- Keep tone calm and reassuring.

Reply strictly in %s.

User message:
%s
""".formatted(language, userMessage);

        String response = llamaService.askLlama(prompt);

        return (response == null || response.isBlank())
                ? fallbackMessage(language)
                : response.trim();
    }

    // 🔹 CHAT
    public String handleChatQuery(String userMessage, String language) {

        String prompt = """
You are a friendly multilingual health chatbot.
Your name is "Asis".
Dont give this information for questions like "what is your name" unless they ask questions about your name like (why asis, what is asis , what is the full form of asis,) you can give this information(Asis is the abbreviation of the names of the founders of this bot. the full form of asis is a=Aadil , s = Shafeeq , i = Ijas , s = Shameem .).

Answer the user's question about yourself.
Explain briefly what you can do.
Do NOT give medical prescriptions.

Reply strictly in %s.

User message:
%s
""".formatted(language, userMessage);

        String response = llamaService.askLlama(prompt);

        return (response == null || response.isBlank())
                ? fallbackMessage(language)
                : response.trim();
    }

    // 🔹 Fallback
    public String fallbackMessage(String language) {

        return switch (language) {

            case "Tamil" ->
                    "மன்னிக்கவும், தற்போது தகவல் கிடைக்கவில்லை. தயவுசெய்து தகுதியான மருத்துவரை அணுகவும்.";

            case "Malayalam" ->
                    "ക്ഷമിക്കണം, ഈ സമയത്ത് വിവരങ്ങൾ ലഭ്യമല്ല. ദയവായി ആരോഗ്യ വിദഗ്ധനെ സമീപിക്കുക.";

            case "Telugu" ->
                    "క్షమించండి, ప్రస్తుతం సమాచారం అందుబాటులో లేదు. దయచేసి వైద్య నిపుణుడిని సంప్రదించండి.";

            default ->
                    "Sorry, I couldn’t find the information right now. Please consult a qualified healthcare professional.";
        };
    }
}