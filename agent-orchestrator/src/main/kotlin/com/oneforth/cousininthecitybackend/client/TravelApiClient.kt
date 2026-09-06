package com.oneforth.cousininthecitybackend.client

import com.oneforth.cousininthecitybackend.model.dtos.FlightResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface TravelApiClient {
    @GetExchange("/api/flights/search")
    @Cacheable("flights")
    fun searchFlights(
        @RequestParam("origin") origin: String,
        @RequestParam("destination") destination: String
    ): List<FlightResponse>
}
