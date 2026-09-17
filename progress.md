# CousinInTheCity - Progress Report

## Architecture Implemented So Far
- **Multi-module Spring Boot Microservices**: The codebase has been fully structured into independent Spring Boot microservices using an **API Gateway / Orchestrator Pattern**.
- **Enterprise Clean Architecture**: Strict separation of concerns is enforced. The Controller Layer only handles HTTP, returning Network DTOs mapped via Kotlin Extension Functions. Business logic strictly resides in the Service Layer, which safely interacts with the Repositories, ensuring zero JPA Entity data leakage to the client.
- **Model Context Protocol (MCP) Design**: Tools and domains are strictly isolated. The central Agent Orchestrator communicates with domain-specific servers via Declarative HTTP Clients (`@GetExchange`).
- **Offline-First Jetpack Compose App**: An Android application built with a local Room SQLite Database that provides an instantaneous chat UI, while seamlessly syncing with the backend APIs.
- **Autonomous Intent Automation**: The Orchestrator forces the LLM to output structured JSON (`AgentResponse`), bridging the gap between natural language and deterministic native OS actions (Google Calendar `ACTION_INSERT`, Google Maps `geo:`, Google Keep `ACTION_SEND`).

## Completed Modules

### 1. `agent-orchestrator` (The AI Brain & API Gateway)
- Migrated from local models to **Google `gemini-3.6-flash`** for superior function calling and larger token context windows.
- Engineered a **Strict JSON Handoff (`AgentResponse`)**: Injected constraints into the System Prompt to force the LLM to return `intentType` (`CALENDAR`, `KEEP`, `MAP`), along with structured temporal data (e.g., `yyyy-MM-dd`) for seamless native OS parsing.
- Connected **PostgreSQL (pgvector)** via JDBC Chat Memory so the AI remembers the conversation context across devices.
- Developed a **RAG KnowledgeBase Pipeline** (`KnowledgeAgentService`) to ingest unstructured text guides into `pgvector` and expose semantic search.
- Engineered a **Redis Caching Layer** using Spring's `@EnableCaching`. The `@GetExchange` HTTP clients aggressively cache downstream microservice JSON responses to eliminate redundant network calls.
- Built an **Asynchronous FCM Push Notification Architecture**: A `NotificationListener` worker consumes messages from a **RabbitMQ** Message Broker (with a Dead Letter Queue) and uses the Firebase Admin SDK to push notifications to specific users based on FCM tokens stored in PostgreSQL. 
- Implemented a `@Scheduled` background **Price Tracker Job** that monitors flights daily at 9:00 AM and queues FCM Push Notifications asynchronously.
- Implemented Swipe-To-Delete functionality via a `DELETE /api/chat/threads/{threadId}` API endpoint.
- Bulletproofed local development using `start-all.ps1`, integrating a `Stop-Port` scanner to securely terminate orphaned Java processes before booting.

### 2. `mcp-travel` (Travel Tool Server)
- Implemented a **Dynamic Mock Engine** in `FlightService.kt` that generates highly realistic randomized domestic flights based on origin/destination without relying on fragile external API keys.

### 3. `mcp-accommodation` (Accommodation Tool Server)
- Integrated the real **OpenStreetMap (Nominatim)** API to geocode the user's destination (fetching exact latitude/longitude).
- Developed a Mock Engine to generate local housing near those coordinates with real, clickable OpenStreetMap URLs in the responses.

### 4. `mcp-finance` (Finance & Budget Tool Server)
- Engineered a **Commute Trade-off Analyzer** which divides cities into Neighborhood Tiers (Premium, Standard, Affordable). 
- Dynamically calculates rent and artificially spikes commute costs if the user chooses a residential tier far from their office tier, solving the real-world problem of "cheap rent vs expensive commute."

### 5. `mcp-location` (Local Transit & Weather Tool Server)
- Developed a **Transit Routing Engine** that accurately maps city-specific transport cultures (e.g., Local Trains in Mumbai, Namma Metro in Bangalore, DMRC in Delhi).
- Integrated external Weather APIs to fetch real-time weather forecasts based on Geocoordinates.

## Critical Architectural Bug Fixes (The Tool-Calling Breakthrough)
1. **The Kotlin `arg0` Reflection Bug**: Injected the `-java-parameters` compiler argument in Gradle so the AI could accurately understand the Spring AI Tool parameters instead of raw `arg0`.
2. **JSON Truncation / Token Limits**: Replaced Ollama due to Jackson parser `Unexpected end-of-input` crashes caused by context limits chopping multi-tool payloads in half.
3. **MIME Type Blocking (Android OS Flaw)**: Resolved an issue where Android blocked `ACTION_INSERT` Calendar Intents by reverting to raw `CONTENT_URI` Data endpoints without strict MIME casting to prevent "No calendars synced" toast failures on physical devices.

## Next Steps
- Android Agent to finish writing the Retrofit integration for the `PUT /fcm-token` and `DELETE /threads` APIs.
