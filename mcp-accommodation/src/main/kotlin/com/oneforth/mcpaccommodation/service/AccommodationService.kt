package com.oneforth.mcpaccommodation.service

import com.oneforth.mcpaccommodation.model.dtos.AccommodationResponse
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import kotlin.random.Random

@Service
class AccommodationService(private val restClient: RestClient) {

    data class NominatimResponse(val lat: String, val lon: String, val display_name: String)

    private val pgBrands = listOf("ZoloStays", "Stanza Living", "CoLive", "Local PG", "YourSpace")
    private val pgSuffixes = listOf("Heights", "Residency", "Homes", "Enclave")

    fun searchAccommodation(officeLocation: String): List<AccommodationResponse> {
        // 1. Hit the real OpenStreetMap API to get exact coordinates of the office
        val nominatimResults = try {
            restClient.get()
                .uri { uriBuilder ->
                    uriBuilder.scheme("https")
                        .host("nominatim.openstreetmap.org")
                        .path("/search")
                        .queryParam("q", officeLocation)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build()
                }
                .header("User-Agent", "CousinInTheCityApp/1.0")
                .retrieve()
                .body(Array<NominatimResponse>::class.java)
        } catch (e: Exception) {
            null
        }

        // Default to center of India if API fails
        val lat = nominatimResults?.firstOrNull()?.lat?.toDoubleOrNull() ?: 20.5937
        val lon = nominatimResults?.firstOrNull()?.lon?.toDoubleOrNull() ?: 78.9629

        val accommodations = mutableListOf<AccommodationResponse>()
        val numResults = Random.nextInt(3, 6)

        for (i in 1..numResults) {
            val brand = pgBrands.random()
            val suffix = pgSuffixes.random()
            val type = if (Random.nextBoolean()) "Single Room" else "Double Sharing"
            val distance = Random.nextInt(500, 5000) // 500m to 5km from office
            val rent = if (type == "Single Room") Random.nextInt(12000, 25000) else Random.nextInt(6000, 11000)

            // Randomize lat/lon slightly to put the PG near the office
            val pgLat = lat + (Random.nextDouble(-0.02, 0.02))
            val pgLon = lon + (Random.nextDouble(-0.02, 0.02))

            // Create a real, clickable OpenStreetMap link!
            val mapLink = "https://www.openstreetmap.org/?mlat=$pgLat&mlon=$pgLon#map=17/$pgLat/$pgLon"

            accommodations.add(
                AccommodationResponse(
                    name = "$brand $suffix",
                    type = type,
                    rentInr = rent,
                    distanceToOfficeMeters = distance,
                    mapLink = mapLink
                )
            )
        }

        return accommodations.sortedBy { it.distanceToOfficeMeters }
    }
}
