package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.LocationApiClient
import com.oneforth.cousininthecitybackend.model.dtos.TransitRouteResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class LocationAgentService(private val client: LocationApiClient) {
    private val logger = LoggerFactory.getLogger(LocationAgentService::class.java)

    @Tool(description = "Get step-by-step local transit routing and commute estimations between two areas in a city.")
    @CircuitBreaker(name = "locationService", fallbackMethod = "fallbackLocation")
    @Retry(name = "locationService", fallbackMethod = "fallbackLocation")
    fun getTransitRoute(city: String, originArea: String, destinationArea: String): TransitRouteResponse? {
        return client.getTransitRoute(city, originArea, destinationArea)
    }

    fun fallbackLocation(city: String, originArea: String, destinationArea: String, t: Throwable): TransitRouteResponse? {
        logger.error("Location Service failed or circuit open. Returning null. Error: ${t.message}")
        return null
    }
}
