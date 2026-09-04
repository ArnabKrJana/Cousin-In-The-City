package com.oneforth.mcptravel.model.dtos

data class FlightResponse(
    val flightNumber: String,
    val airline: String,
    val origin: String,
    val destination: String,
    val departureTime: String,
    val arrivalTime: String,
    val priceInr: Int
)
