package com.oneforth.mcptravel.service

import com.oneforth.mcptravel.model.dtos.FlightResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

@Service
class FlightService {

    private val airlines = listOf("Indigo", "Air India", "Vistara", "SpiceJet", "Akasa Air")
    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun searchFlights(origin: String, destination: String): List<FlightResponse> {
        val numFlights = Random.nextInt(3, 7) // Generate 3 to 6 random flights
        val flights = mutableListOf<FlightResponse>()
        
        val baseTime = LocalDateTime.now().plusDays(Random.nextLong(1, 14)) // Random day in the next two weeks

        for (i in 1..numFlights) {
            val airline = airlines.random()
            val flightNum = "${airline.substring(0, 2).uppercase()}-${Random.nextInt(100, 999)}"
            val price = Random.nextInt(3500, 12000) // Realistic domestic prices
            
            // Randomize departure time
            val departure = baseTime.plusHours(Random.nextLong(1, 12)).plusMinutes(Random.nextLong(0, 59))
            // Assume 2 to 3 hour flight duration
            val arrival = departure.plusHours(Random.nextLong(2, 3)).plusMinutes(Random.nextLong(10, 50))

            flights.add(
                FlightResponse(
                    flightNumber = flightNum,
                    airline = airline,
                    origin = origin.uppercase(),
                    destination = destination.uppercase(),
                    departureTime = departure.format(timeFormatter),
                    arrivalTime = arrival.format(timeFormatter),
                    priceInr = price
                )
            )
        }
        
        // Sort by price (cheapest first)
        return flights.sortedBy { it.priceInr }
    }
}
