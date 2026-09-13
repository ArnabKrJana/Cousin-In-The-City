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
