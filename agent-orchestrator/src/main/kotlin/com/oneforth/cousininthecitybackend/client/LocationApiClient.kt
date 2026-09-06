package com.oneforth.cousininthecitybackend.client

import com.oneforth.cousininthecitybackend.model.dtos.TransitRouteResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface LocationApiClient {
    @GetExchange("/api/location/transit-route")
    @Cacheable("locations")
    fun getTransitRoute(
        @RequestParam("city") city: String,
        @RequestParam("originArea") originArea: String,
        @RequestParam("destinationArea") destinationArea: String
    ): TransitRouteResponse
}
