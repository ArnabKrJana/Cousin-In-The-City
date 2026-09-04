package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.TravelApiClient
import com.oneforth.cousininthecitybackend.model.dtos.FlightResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class TravelAgentService(private val client: TravelApiClient) {

    @Tool(description = "Search for available domestic flights between two cities")
    fun searchFlights(origin: String, destination: String): List<FlightResponse> {
        return try {
            client.searchFlights(origin, destination)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
