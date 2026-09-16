package com.oneforth.cousininthecitybackend.controller

import com.oneforth.cousininthecitybackend.model.dtos.*
import com.oneforth.cousininthecitybackend.service.ChatService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService
) {

    @PostMapping("/users/{deviceId}")
    fun registerUser(@PathVariable deviceId: String): AppUserDto {
        return chatService.registerUser(deviceId)
    }

    @PutMapping("/users/{deviceId}/fcm-token")
    fun updateFcmToken(@PathVariable deviceId: String, @RequestParam token: String): org.springframework.http.ResponseEntity<Void> {
        chatService.updateFcmToken(deviceId, token)
        return org.springframework.http.ResponseEntity.ok().build()
    }

    @GetMapping("/users/{deviceId}/threads")
    fun getThreads(@PathVariable deviceId: String): List<ChatThreadDto> {
        return chatService.getThreads(deviceId)
    }

    @PostMapping("/users/{deviceId}/threads")
    fun createThread(@PathVariable deviceId: String, @RequestParam title: String): ChatThreadDto {
        return chatService.createThread(deviceId, title)
    }

    @GetMapping("/history/{threadId}")
    fun getHistory(@PathVariable threadId: String): List<MessageDto> {
        return chatService.getHistory(threadId)
    }

    @PostMapping
    fun chat(@RequestBody input: ChatInput): AgentResponse {
        return chatService.chat(input)
    }

    @DeleteMapping("/threads/{threadId}")
    fun deleteThread(@PathVariable threadId: String): org.springframework.http.ResponseEntity<Void> {
        chatService.deleteThread(threadId)
        return org.springframework.http.ResponseEntity.noContent().build()
    }
}

