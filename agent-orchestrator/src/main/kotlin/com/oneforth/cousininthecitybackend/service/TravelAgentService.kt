package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.TravelApiClient
import com.oneforth.cousininthecitybackend.model.dtos.FlightResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class TravelAgentService(private val client: TravelApiClient) {
    private val logger = LoggerFactory.getLogger(TravelAgentService::class.java)

    @Tool(description = "Search for available domestic flights between two cities")
    @CircuitBreaker(name = "travelService", fallbackMethod = "fallbackFlights")
    @Retry(name = "travelService", fallbackMethod = "fallbackFlights")
    fun searchFlights(origin: String, destination: String): List<FlightResponse> {
        return client.searchFlights(origin, destination)
    }

    // Fallback method must match the exact signature plus the Exception parameter
    fun fallbackFlights(origin: String, destination: String, t: Throwable): List<FlightResponse> {
        logger.error("Travel Service failed or circuit open. Returning empty list. Error: ${t.message}")
        return emptyList()
    }
}
