# Cousin In The City 🏙️✈️

**An Autonomous, Agentic AI Travel Assistant powered by Spring Boot Microservices and Google Gemini.**

📱 **Android Client Repository:** [Cousin-In-The-City-Android](https://github.com/ArnabKrJana/Cousin-In-The-City-Android)

🎥 **Working Demo Video:** [Watch on YouTube](https://youtu.be/uh3yX3Bqu-w)
<br>
<a href="https://youtu.be/uh3yX3Bqu-w" target="_blank">
  <img src="https://img.youtube.com/vi/uh3yX3Bqu-w/maxresdefault.jpg" alt="Working Demo Video" width="600"/>
</a>

![Kotlin](https://img.shields.io/badge/Kotlin-2.3+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1+-brightgreen.svg)
![Spring AI](https://img.shields.io/badge/Spring_AI-2.0+-green.svg)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Message_Broker-orange.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-pgvector-blue.svg)

"Cousin In The City" is not a standard chatbot. It is a proactive, autonomous AI agent that acts like a local cousin living in your destination city. It bridges the gap between natural language processing and deterministic OS-level execution by securely translating conversational requests into physical Android Intents (Google Calendar, Keep, Maps) and running autonomous background jobs to monitor travel data while the app is closed.

---

## ✨ Key Features

* **Agentic Tool Calling (MCP):** Powered by **Google Gemini** and **Spring AI**, the Orchestrator dynamically routes user queries to specialized domain microservices (Travel, Accommodation, Finance, Location) to fetch real-time data.
* **Native Android OS Automation:** The AI strictly outputs constrained JSON payloads (`AgentResponse`), allowing the Jetpack Compose Android client to automatically launch Google Calendar (for itineraries), Google Maps (for navigation), or Google Keep (for saving notes) without manual user data entry.
* **Proactive FCM Alerts (Event-Driven):** A Spring `@Scheduled` background cron job tracks flight prices autonomously. It drops payloads into a **RabbitMQ** Message Broker, which a worker thread consumes to blast **Firebase Cloud Messaging (FCM)** push notifications, waking up the user's phone when deals are found.
* **Enterprise Clean Architecture:** Strict Separation of Concerns. The REST Controllers are completely decoupled from the PostgreSQL schema via Network DTOs and Kotlin Extension Functions, preventing data leakage.
* **RAG & Chat Memory:** Utilizes **Redis** for hyper-fast conversational memory caching and **PostgreSQL (pgvector)** for long-term Retrieval-Augmented Generation (RAG) context.

---

## 🏗️ Architecture

The backend utilizes the **API Gateway / Orchestrator Pattern** to securely manage domain microservices. 

*   `agent-orchestrator`: The central API Gateway and AI Brain.
*   `mcp-travel`: Mock engine for domestic flight generation.
*   `mcp-accommodation`: Integrates OpenStreetMap (Nominatim) API for exact geocoding.
*   `mcp-location`: Calculates dynamic transit routes and queries real-time Weather APIs.
*   `mcp-finance`: Calculates Commute vs. Rent trade-offs based on city neighborhood tiers.

👉 **[View the full Mermaid Architecture Diagram & Workflow here](ARCHITECTURE.md)**

---

## 🚀 Getting Started (Local Development)

### Prerequisites
* Java 25 / Kotlin 2.3+
* Docker Desktop
* Firebase Service Account (`firebase-admin.json`)
* Gemini API Key

### 1. Configure Secrets
Place your Google Gemini API key inside `agent-orchestrator/src/main/resources/application.yaml`:
```yaml
spring:
  ai:
    google:
      genai:
        api-key: YOUR_GEMINI_KEY_HERE
```
Place your Firebase Admin SDK key at `agent-orchestrator/src/main/resources/firebase-admin.json`.

### 2. Boot Infrastructure
Spin up the PostgreSQL, Redis, and RabbitMQ containers:
```bash
docker-compose up -d
```

### 3. Start the Microservices
A custom PowerShell script is provided to automatically scan for orphaned Java ports, safely kill them, and boot the Orchestrator alongside all MCP microservices:
```powershell
./start-all.ps1
```

---

## 📖 Documentation
* **[Progress Tracker](progress.md)**: Detailed milestone tracking.

