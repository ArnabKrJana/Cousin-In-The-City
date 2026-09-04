package com.oneforth.cousininthecitybackend.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/chat")
class ChatController(
    chatClientBuilder: ChatClient.Builder,
    chatMemory: ChatMemory
) {

    data class ChatInput(val prompt: String, val conversationId: String = "default-user")
    data class ChatOutput(val content: String)

    private val chatClient: ChatClient = chatClientBuilder.build()
    private val memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build()

    @PostMapping
    fun chat(@RequestBody input: ChatInput): ChatOutput {
        val response = chatClient.prompt()
            .user(input.prompt)
            .advisors(memoryAdvisor)
            .advisors { a -> 
                a.param("chat_memory_conversation_id", input.conversationId) 
            }
            .call()
            .content() ?: "I'm sorry, I couldn't process that."
            
        return ChatOutput(response)
    }
}
