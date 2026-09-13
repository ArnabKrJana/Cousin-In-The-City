package com.oneforth.cousininthecitybackend.controller

import com.oneforth.cousininthecitybackend.model.entity.AppUser
import com.oneforth.cousininthecitybackend.model.entity.ChatThread
import com.oneforth.cousininthecitybackend.repository.AppUserRepository
import com.oneforth.cousininthecitybackend.repository.ChatThreadRepository
import com.oneforth.cousininthecitybackend.model.dtos.AgentResponse
import com.oneforth.cousininthecitybackend.model.dtos.ChatInput
import com.oneforth.cousininthecitybackend.model.dtos.MessageDto
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatClient: ChatClient,
    private val chatMemory: ChatMemory,
    private val userRepository: AppUserRepository,
    private val threadRepository: ChatThreadRepository
) {

    private val memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build()

    // 1. Register a new Android Device / User
    @PostMapping("/users/{deviceId}")
    fun registerUser(@PathVariable deviceId: String): AppUser {
        return userRepository.findById(deviceId).orElseGet {
            userRepository.save(AppUser(deviceId = deviceId))
        }
    }

    // 2. Fetch all threads for a device
    @GetMapping("/users/{deviceId}/threads")
    fun getThreads(@PathVariable deviceId: String): List<ChatThread> {
        return threadRepository.findByDeviceIdOrderByUpdatedAtDesc(deviceId)
    }

    // 3. Create a new thread
    @PostMapping("/users/{deviceId}/threads")
    fun createThread(@PathVariable deviceId: String, @RequestParam title: String): ChatThread {
        return threadRepository.save(ChatThread(deviceId = deviceId, title = title))
    }

    // 4. Fetch Chat History from Spring AI ChatMemory
    @GetMapping("/history/{threadId}")
    fun getHistory(@PathVariable threadId: String): List<MessageDto> {
        val messages = chatMemory.get(threadId)
        return messages.map { 
            MessageDto(role = it.messageType.name, content = it.text ?: "") 
        }
    }

    // 5. Send a Chat Message
    @PostMapping
    fun chat(@RequestBody input: ChatInput): AgentResponse {
        // Update the thread's updatedAt timestamp
        threadRepository.findById(input.conversationId).ifPresent { thread ->
            thread.updatedAt = LocalDateTime.now()
            threadRepository.save(thread)
        }

        return try {
            chatClient.prompt()
                .user(input.prompt)
                .advisors(memoryAdvisor)
                .advisors { a -> 
                    a.param("chat_memory_conversation_id", input.conversationId) 
                }
                .call()
                .entity(AgentResponse::class.java) 
                ?: AgentResponse("Sorry, I could not process that.", null, null)
        } catch (e: Exception) {
            // Fallback if structured output fails
            AgentResponse("Error processing request: ${e.message}", null, null)
        }
    }
}
