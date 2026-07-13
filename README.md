# 🏥 ASIS

A Hybrid LLM and Knowledge-Base Multilingual Public Health WhatsApp Chatbot with Real-Time Disease Outbreak Monitoring. Built to make public health information accessible directly over WhatsApp, in the languages people actually speak.

## 📦 Tech Stack
Java 21 · Spring Boot 4.1.0 · Spring Data JPA · SQL · MySQL 8.0 · Llama 3.3-70B (HuggingFace) · Twilio WhatsApp API · Jsoup

## 🦄 Features
- **Multilingual support**: English, Tamil, Tanglish, Malayalam, Manglish, and Telugu
- **WhatsApp-native**: interact entirely through WhatsApp via the Twilio API — no separate app needed
- **Hybrid intelligence**: combines a curated knowledge base with an LLM (Llama 3.3-70B) for accurate, context-aware health answers
- **Real-time outbreak monitoring**: scrapes live data from government health portals (IDSP, NCDC) using Jsoup to keep users informed of ongoing disease outbreaks

## 👩🏽‍🍳 The Process
The project started with building a reliable knowledge base of public health information, then layering an LLM on top to handle natural, conversational queries in multiple languages and dialects.

Real-time outbreak monitoring was added by scraping IDSP and NCDC portals, feeding fresh outbreak data back into chatbot responses. Twilio's WhatsApp API was integrated so the whole experience runs where people already are — no new app to download or learn.

## 🚦 Getting Started
```bash
git clone <repo-url>
cd asis
# configure application.properties with your MySQL, Twilio, and HuggingFace credentials
./mvnw spring-boot:run
```

## 💭 Future Improvements
- Expand outbreak data sources beyond IDSP/NCDC
- Add voice-note query support
- Support more regional languages and dialects

## 🖼️ Screenshots

<img width="250" height="600" alt="WhatsApp Image 2026-07-12 at 1 00 46 PM (2)" src="https://github.com/user-attachments/assets/59e3cca7-9f30-44c2-b483-3f7d9ec202b5" />
<img width="250" height="600" alt="WhatsApp Image 2026-07-12 at 1 00 46 PM (3)" src="https://github.com/user-attachments/assets/b2917768-ba75-41be-b426-3e41b0e617f2" />
<img width="250" height="600" alt="WhatsApp Image 2026-07-12 at 1 00 46 PM" src="https://github.com/user-attachments/assets/0e46e2d8-c611-467f-a13e-662bc6b520a2" />
