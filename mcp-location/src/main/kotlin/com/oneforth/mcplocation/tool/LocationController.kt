package com.oneforth.mcplocation.tool

import com.oneforth.mcplocation.model.dtos.TransitRouteResponse
import com.oneforth.mcplocation.model.dtos.WeatherResponse
import com.oneforth.mcplocation.service.TransitService
import com.oneforth.mcplocation.service.WeatherService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/location")
class LocationController(
    private val transitService: TransitService,
    private val weatherService: WeatherService
) {

    @GetMapping("/transit-route")
    fun getTransitRoute(
        @RequestParam city: String,
        @RequestParam originArea: String,
        @RequestParam destinationArea: String
    ): TransitRouteResponse {
        return transitService.getTransitRoute(city, originArea, destinationArea)
    }

    @GetMapping("/weather")
    fun getWeather(@RequestParam location: String): WeatherResponse {
        return weatherService.getCurrentWeather(location)
    }
}
