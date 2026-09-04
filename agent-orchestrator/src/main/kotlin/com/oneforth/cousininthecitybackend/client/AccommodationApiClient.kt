package com.oneforth.cousininthecitybackend.client

import com.oneforth.cousininthecitybackend.model.dtos.AccommodationResponse
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface AccommodationApiClient {
    @GetExchange("/api/accommodation/search")
    fun searchAccommodation(
        @RequestParam("officeLocation") officeLocation: String
    ): List<AccommodationResponse>
}
