package com.oneforth.mcpaccommodation.tool

import com.oneforth.mcpaccommodation.model.dtos.AccommodationResponse
import com.oneforth.mcpaccommodation.service.AccommodationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/accommodation")
class AccommodationController(private val accommodationService: AccommodationService) {

    @GetMapping("/search")
    fun searchAccommodation(
        @RequestParam officeLocation: String
    ): List<AccommodationResponse> {
        return accommodationService.searchAccommodation(officeLocation)
    }
}
