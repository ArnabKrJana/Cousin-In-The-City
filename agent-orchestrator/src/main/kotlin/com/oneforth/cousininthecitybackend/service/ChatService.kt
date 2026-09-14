package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.model.entity.AppUser
import com.oneforth.cousininthecitybackend.model.entity.ChatThread
import com.oneforth.cousininthecitybackend.repository.AppUserRepository
import com.oneforth.cousininthecitybackend.repository.ChatThreadRepository
import com.oneforth.cousininthecitybackend.model.dtos.*
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatService(
    private val chatClient: ChatClient,
    private val chatMemory: ChatMemory,
    private val userRepository: AppUserRepository,
    private val threadRepository: ChatThreadRepository
) {
    private val memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build()

    fun registerUser(deviceId: String): AppUserDto {
        val user = userRepository.findById(deviceId).orElseGet {
            userRepository.save(AppUser(deviceId = deviceId))
        }
        return user.toDto()
    }

    fun getThreads(deviceId: String): List<ChatThreadDto> {
        return threadRepository.findByDeviceIdOrderByUpdatedAtDesc(deviceId).map { it.toDto() }
    }

    fun createThread(deviceId: String, title: String): ChatThreadDto {
        val thread = threadRepository.save(ChatThread(deviceId = deviceId, title = title))
        return thread.toDto()
    }

    fun getHistory(threadId: String): List<MessageDto> {
        val messages = chatMemory.get(threadId)
        return messages.map { 
            MessageDto(role = it.messageType.name, content = it.text ?: "") 
        }
    }

    fun chat(input: ChatInput): AgentResponse {
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
            AgentResponse("Error processing request: ${e.message}", null, null)
        }
    }

    fun deleteThread(threadId: String) {
        if (threadRepository.existsById(threadId)) {
            threadRepository.deleteById(threadId)
        }
        chatMemory.clear(threadId)
    }
}
