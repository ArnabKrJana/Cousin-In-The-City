package com.oneforth.cousininthecitybackend.model.dtos

import com.fasterxml.jackson.annotation.JsonPropertyDescription

data class ChatInput(
    val prompt: String, 
    val conversationId: String = "default-user"
)
    
data class AgentResponse(
    @field:JsonPropertyDescription("The friendly conversational response to the user.")
    val message: String,
    
    @field:JsonPropertyDescription("The Android Intent to trigger. Can be CALENDAR, MAP, or KEEP. Null if no intent is requested.")
    val intentType: String? = null,
    
    @field:JsonPropertyDescription("Data for the intent. e.g. 'title' and 'date' for CALENDAR, 'location' for MAP.")
    val actionData: Map<String, String>? = null
)
    
data class MessageDto(
    val role: String, 
    val content: String
)

data class AppUserDto(
    val deviceId: String,
    val createdAt: java.time.LocalDateTime
)

data class ChatThreadDto(
    val threadId: String,
    val deviceId: String,
    val title: String,
    val createdAt: java.time.LocalDateTime,
    val updatedAt: java.time.LocalDateTime
)

fun com.oneforth.cousininthecitybackend.model.entity.AppUser.toDto() = AppUserDto(
    deviceId = this.deviceId,
    createdAt = this.createdAt
)

fun com.oneforth.cousininthecitybackend.model.entity.ChatThread.toDto() = ChatThreadDto(
    threadId = this.threadId,
    deviceId = this.deviceId,
    title = this.title,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
