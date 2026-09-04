package com.oneforth.mcptravel.tool

import com.oneforth.mcptravel.model.dtos.FlightResponse
import com.oneforth.mcptravel.service.FlightService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/flights")
class FlightController(private val flightService: FlightService) {

    @GetMapping("/search")
    fun searchFlights(
        @RequestParam origin: String,
        @RequestParam destination: String
    ): List<FlightResponse> {
        return flightService.searchFlights(origin, destination)
    }
}
