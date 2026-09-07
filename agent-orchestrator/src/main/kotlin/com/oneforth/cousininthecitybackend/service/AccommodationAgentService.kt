package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.AccommodationApiClient
import com.oneforth.cousininthecitybackend.model.dtos.AccommodationResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class AccommodationAgentService(private val client: AccommodationApiClient) {
    private val logger = LoggerFactory.getLogger(AccommodationAgentService::class.java)

    @Tool(description = "Search for PG, hostels, or apartments near a specific office location")
    @CircuitBreaker(name = "accommService", fallbackMethod = "fallbackAccommodation")
    @Retry(name = "accommService", fallbackMethod = "fallbackAccommodation")
    fun searchAccommodation(officeLocation: String): List<AccommodationResponse> {
        return client.searchAccommodation(officeLocation)
    }

    fun fallbackAccommodation(officeLocation: String, t: Throwable): List<AccommodationResponse> {
        logger.error("Accommodation Service failed or circuit open. Returning empty list. Error: ${t.message}")
        return emptyList()
    }
}
