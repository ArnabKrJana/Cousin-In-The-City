package com.oneforth.mcplocation.model.dtos

data class RouteOption(
    val primaryMode: String,
    val estimatedTimeMinutes: Int,
    val estimatedCostInr: Int,
    val stepByStepInstructions: List<String>
)

data class TransitRouteResponse(
    val city: String,
    val originArea: String,
    val destinationArea: String,
    val cityTransportSystems: List<String>,
    val recommendedRoute: RouteOption,
    val alternativeRoute: RouteOption
)

data class WeatherResponse(
    val location: String,
    val temperatureCelsius: Double,
    val condition: String,
    val humidity: Int
)
