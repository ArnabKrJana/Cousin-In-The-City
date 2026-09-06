# CousinInTheCity - Progress Report

## Architecture Implemented So Far
- **Multi-module Spring Boot Microservices**: The codebase has been fully structured into independent Spring Boot microservices.
- **Clean Architecture & MVC**: Strict separation of concerns is enforced. Business logic resides in `@Service` classes, HTTP mapping in `@RestController` classes, and standard data models in `model/dtos`.
- **Model Context Protocol (MCP) Design**: Tools and domains are strictly isolated. The central Agent Orchestrator communicates with domain-specific servers via Declarative HTTP Clients (`@GetExchange`).

## Completed Modules

### 1. `agent-orchestrator` (The AI Brain)
- Configured Spring AI `ChatClient` with the local **Ollama (`llama3.2:3b`)** model.
- Connected **PostgreSQL (pgvector)** via JDBC Chat Memory so the AI remembers the conversation context.
- Implemented a frontend-driven dynamic `sessionId` generation in `chat.html` to guarantee thread-isolated memory inside PostgreSQL.
- Developed a **RAG KnowledgeBase Pipeline** (`KnowledgeAgentService`) to ingest unstructured text guides into `pgvector` and expose semantic search to the AI using `Top-K` filtering.
- Implemented a **Time Engine** (`TimeAgentService`) giving the AI real-world clock awareness for relative queries (e.g. "tomorrow").
- Registered AI tools (`@Tool`) pointing to the domain microservices via `HttpServiceProxyFactory`.

### 2. `mcp-travel` (Travel Tool Server)
- Exposed the `/api/flights/search` API.
- Implemented a **Dynamic Mock Engine** in `FlightService.kt` that generates highly realistic randomized domestic flights (Indigo, Air India, Vistara) based on origin/destination without relying on fragile external API keys.

### 3. `mcp-accommodation` (Accommodation Tool Server)
- Exposed the `/api/accommodation/search` API.
- Integrated the real **OpenStreetMap (Nominatim)** API to geocode the user's destination (fetching exact latitude/longitude).
- Developed a Mock Engine to generate local housing (PGs, Hostels) near those coordinates and embedded real, clickable OpenStreetMap URLs in the responses.

### 4. `mcp-finance` (Finance & Budget Tool Server)
- Exposed the `/api/finance/neighborhood-budget` API.
- Engineered a **Commute Trade-off Analyzer** which divides cities into Neighborhood Tiers (Premium, Standard, Affordable). 
- Dynamically calculates rent and artificially spikes commute costs if the user chooses a residential tier far from their office tier, solving the real-world problem of "cheap rent vs expensive commute."
- Tags recommendations as "Over Budget," "Tight," or "Comfortable" to inform the AI's downstream logic.

### 5. `mcp-location` (Local Transit Tool Server)
- Exposed the `/api/location/transit-route` API.
- Developed a **Transit Routing Engine** that accurately maps city-specific transport cultures (e.g., Local Trains in Mumbai, Namma Metro in Bangalore, DMRC in Delhi).
- Dynamically compares a cheap Public Transit multi-leg route (e.g., Auto -> Train) against an expensive Alternative Route (e.g., Uber/Cab), providing step-by-step instructions and cost estimations for the AI to present to the user.

## Critical Architectural Bug Fixes (The Tool-Calling Breakthrough)
During testing, the AI model initially failed to execute the Multi-Agent tools. We successfully diagnosed and resolved four critical system design bugs:
1. **The Kotlin `arg0` Reflection Bug**: Kotlin compiles parameter names into generic `arg0`, `arg1`, stripping them from the Spring AI Tool schemas. We injected the `-java-parameters` compiler argument in Gradle and performed a clean build so the AI could accurately understand the tool parameters.
2. **The Clean Architecture Dependency Bug**: The `ChatController` was incorrectly instantiating a raw, empty `ChatClient.Builder`, completely bypassing the tools and system prompts configured in our `AiConfig` `@Bean`. We refactored the controller to properly inject the configured `ChatClient`.
3. **The Poisoned JDBC Memory Bug**: After early hallucination failures, massive 4000-token hallucinated logs were stored in PostgreSQL. These were recursively injected into the prompt, overflowing the 4096 context limit instantly. We implemented dynamic Session IDs in the UI to ensure clean memory contexts.
4. **Model Capability Upgrade**: Small 2B parameter models (`gemma`) lack the reasoning capacity to choose between 4 complex microservices simultaneously. We migrated the Orchestrator to Meta's officially supported **`llama3.2:3b`** model with `temperature: 0.1` to ensure strict, deterministic tool routing within a 4GB VRAM constraint.

## Next Steps
- Build the Android Native (Jetpack Compose) frontend to consume the Orchestrator's APIs.
