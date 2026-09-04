# CousinInTheCity - Progress Report

## Architecture Implemented So Far
- **Multi-module Spring Boot Microservices**: The codebase has been fully structured into independent Spring Boot microservices.
- **Clean Architecture & MVC**: Strict separation of concerns is enforced. Business logic resides in `@Service` classes, HTTP mapping in `@RestController` classes, and standard data models in `model/dtos`.
- **Model Context Protocol (MCP) Design**: Tools and domains are strictly isolated. The central Agent Orchestrator communicates with domain-specific servers via Declarative HTTP Clients (`@GetExchange`).

## Completed Modules

### 1. `agent-orchestrator` (The AI Brain)
- Configured Spring AI `ChatClient` with the local **Ollama (`gemma4:e2b`)** model.
- Connected **PostgreSQL (pgvector)** via JDBC Chat Memory so the AI remembers the conversation context.
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

## Next Steps
- Implement `mcp-location` (Local transit and commute logic).
- Build the Android Native (Jetpack Compose) frontend to consume the Orchestrator's APIs.
