package com.oneforth.cousininthecitybackend.config

import com.oneforth.cousininthecitybackend.service.AccommodationAgentService
import com.oneforth.cousininthecitybackend.service.FinanceAgentService
import com.oneforth.cousininthecitybackend.service.LocationAgentService
import com.oneforth.cousininthecitybackend.service.TravelAgentService
import com.oneforth.cousininthecitybackend.service.KnowledgeAgentService
import com.oneforth.cousininthecitybackend.service.TimeAgentService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.memory.MessageWindowChatMemory
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.Resource

@Configuration
class AiConfig {

    @Value("classpath:/prompts/system-prompt.st")
    private lateinit var systemPromptResource: Resource

    @Bean
    fun providesMessageWindowChatMemory(jdbcChatMemoryRepository: JdbcChatMemoryRepository): MessageWindowChatMemory {
        // Reduced from 100 to 10 to prevent 4096 context limit truncation
        val maxMessage = 10
        return MessageWindowChatMemory.builder()
            .chatMemoryRepository(jdbcChatMemoryRepository)
            .maxMessages(maxMessage)
            .build()
    }

    @Primary
    @Bean
    fun providesChatMemory(messageWindowChatMemory: MessageWindowChatMemory): ChatMemory {
        return messageWindowChatMemory
    }

    @Bean
    fun chatClient(
        builder: ChatClient.Builder,
        travelAgentService: TravelAgentService,
        accommodationAgentService: AccommodationAgentService,
        financeAgentService: FinanceAgentService,
        locationAgentService: LocationAgentService,
        timeAgentService: TimeAgentService,
        knowledgeAgentService: KnowledgeAgentService
    ): ChatClient {
        return builder
            .defaultSystem(systemPromptResource)
            .defaultTools(travelAgentService, accommodationAgentService, financeAgentService, locationAgentService, timeAgentService, knowledgeAgentService)
            .build()
    }
}
