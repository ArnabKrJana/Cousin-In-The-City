package com.oneforth.cousininthecitybackend.model.dtos

data class NotificationMessage(
    val deviceId: String,
    val title: String,
    val body: String
)
