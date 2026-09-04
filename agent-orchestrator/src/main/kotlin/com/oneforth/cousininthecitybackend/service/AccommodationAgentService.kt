package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.AccommodationApiClient
import com.oneforth.cousininthecitybackend.model.dtos.AccommodationResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class AccommodationAgentService(private val client: AccommodationApiClient) {

    @Tool(description = "Search for PG, hostels, or apartments near a specific office location")
    fun searchAccommodation(officeLocation: String): List<AccommodationResponse> {
        return try {
            client.searchAccommodation(officeLocation)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
