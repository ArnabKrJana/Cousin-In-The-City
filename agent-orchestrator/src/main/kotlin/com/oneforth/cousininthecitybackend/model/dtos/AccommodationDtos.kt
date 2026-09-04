package com.oneforth.cousininthecitybackend.model.dtos

data class AccommodationResponse(
    val name: String,
    val type: String,
    val rentInr: Int,
    val distanceToOfficeMeters: Int,
    val mapLink: String
)
