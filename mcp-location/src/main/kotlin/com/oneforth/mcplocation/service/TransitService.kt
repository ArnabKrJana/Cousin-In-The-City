package com.oneforth.mcplocation.service

import com.oneforth.mcplocation.model.dtos.RouteOption
import com.oneforth.mcplocation.model.dtos.TransitRouteResponse
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class TransitService {

    private val cityTransportData = mapOf(
        "MUMBAI" to listOf("Local Trains", "BEST Buses", "Auto Rickshaws", "Metro"),
        "BANGALORE" to listOf("Namma Metro", "BMTC Volvo Buses", "Auto/Rapido"),
        "DELHI" to listOf("DMRC Metro", "DTC Buses", "E-Rickshaws")
    )

    fun getTransitRoute(city: String, originArea: String, destinationArea: String): TransitRouteResponse {
        val normalizedCity = city.uppercase()
        val systems = cityTransportData[normalizedCity] ?: listOf("Buses", "Taxis", "Auto Rickshaws")

        val recommendedRoute = generateRecommendedRoute(normalizedCity, originArea, destinationArea, systems)
        val alternativeRoute = generateAlternativeRoute(originArea, destinationArea)

        return TransitRouteResponse(
            city = city,
            originArea = originArea,
            destinationArea = destinationArea,
            cityTransportSystems = systems,
            recommendedRoute = recommendedRoute,
            alternativeRoute = alternativeRoute
        )
    }

    private fun generateRecommendedRoute(city: String, origin: String, dest: String, systems: List<String>): RouteOption {
        val time = Random.nextInt(35, 75)
        val cost = Random.nextInt(20, 60)

        val mode = when (city) {
            "MUMBAI" -> "Auto + Local Train"
            "BANGALORE" -> "Rapido + Namma Metro"
            "DELHI" -> "E-Rickshaw + DMRC Metro"
            else -> "Bus + Walk"
        }

        val instructions = when (city) {
            "MUMBAI" -> listOf(
                "Take a shared auto from $origin to the nearest local train station.",
                "Board a Fast/Slow local train heading towards $dest.",
                "Alight at the station closest to $dest and walk for 5 mins."
            )
            "BANGALORE" -> listOf(
                "Book a Rapido bike or Auto to the nearest Purple/Green line Metro station.",
                "Take the Namma Metro towards the $dest station.",
                "Exit the station and walk 10 mins to the office."
            )
            "DELHI" -> listOf(
                "Take an E-Rickshaw to the nearest DMRC Metro station.",
                "Board the metro on the appropriate color line to $dest.",
                "Exit at $dest gate and walk 2 mins."
            )
            else -> listOf(
                "Take a local city bus from $origin.",
                "Drop at $dest bus stop.",
                "Walk to your destination."
            )
        }

        return RouteOption(
            primaryMode = mode,
            estimatedTimeMinutes = time,
            estimatedCostInr = cost,
            stepByStepInstructions = instructions
        )
    }

    private fun generateAlternativeRoute(origin: String, dest: String): RouteOption {
        // Cab/Uber is always the expensive, potentially slower alternative due to traffic
        val time = Random.nextInt(55, 120)
        val cost = Random.nextInt(350, 750)

        return RouteOption(
            primaryMode = "Uber / Ola Cab",
            estimatedTimeMinutes = time,
            estimatedCostInr = cost,
            stepByStepInstructions = listOf(
                "Book an Uber/Ola from your location at $origin.",
                "Sit in traffic.",
                "Arrive directly at the office in $dest."
            )
        )
    }
}
