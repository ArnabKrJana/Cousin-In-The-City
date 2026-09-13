package com.oneforth.mcplocation.service

import com.oneforth.mcplocation.model.dtos.WeatherResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class WeatherService(
    @Value("\${weather.api-key}") private val apiKey: String
) {
    private val logger = LoggerFactory.getLogger(WeatherService::class.java)
    private val restClient = RestClient.create()

    fun getCurrentWeather(location: String): WeatherResponse {
        logger.info("Fetching weather for $location from WeatherAPI")
        
        try {
            val response = restClient.get()
                .uri("https://api.weatherapi.com/v1/current.json?key={key}&q={q}", apiKey, location)
                .retrieve()
                .body(Map::class.java)

            if (response != null) {
                val current = response["current"] as? Map<*, *>
                val tempC = (current?.get("temp_c") as? Number)?.toDouble() ?: 0.0
                val condition = (current?.get("condition") as? Map<*, *>)?.get("text") as? String ?: "Unknown"
                val humidity = (current?.get("humidity") as? Number)?.toInt() ?: 0

                return WeatherResponse(
                    location = location,
                    temperatureCelsius = tempC,
                    condition = condition,
                    humidity = humidity
                )
            }
        } catch (e: Exception) {
            logger.error("Failed to fetch weather: ${e.message}")
        }
        
        // Fallback or error response
        return WeatherResponse(location, 0.0, "Weather Data Unavailable", 0)
    }
}
