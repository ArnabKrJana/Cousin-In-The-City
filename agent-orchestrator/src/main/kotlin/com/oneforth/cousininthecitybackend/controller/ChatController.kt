package com.oneforth.cousininthecitybackend.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ChatController(
    chatClientBuilder: ChatClient.Builder,
    chatMemory: ChatMemory,
    vectorStore: VectorStore
) {

    private val chatClient: ChatClient = chatClientBuilder.build()
    private val memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build()
    // TODO: RAG Advisor configuration pending proper dependency resolution in Spring AI 2.0.1

    @GetMapping("/chat")
    fun chat(
        @RequestParam(value = "message", defaultValue = "Hello Cousin!") message: String,
        @RequestParam(value = "chatId", defaultValue = "default-user") chatId: String
    ): String {
        return chatClient.prompt()
            .user(message)
            .advisors(memoryAdvisor)
            .advisors { a -> 
                a.param("chat_memory_conversation_id", chatId) 
            }
            .call()
            .content() ?: "I'm sorry, I couldn't process that."
    }
}
