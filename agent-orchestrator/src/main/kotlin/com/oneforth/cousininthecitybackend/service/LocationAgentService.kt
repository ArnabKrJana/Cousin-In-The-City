package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.LocationApiClient
import com.oneforth.cousininthecitybackend.model.dtos.TransitRouteResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class LocationAgentService(private val client: LocationApiClient) {

    @Tool(description = "Get step-by-step local transit routing and commute estimations between two areas in a city.")
    fun getTransitRoute(city: String, originArea: String, destinationArea: String): TransitRouteResponse? {
        return try {
            client.getTransitRoute(city, originArea, destinationArea)
        } catch (e: Exception) {
            null
        }
    }
}
