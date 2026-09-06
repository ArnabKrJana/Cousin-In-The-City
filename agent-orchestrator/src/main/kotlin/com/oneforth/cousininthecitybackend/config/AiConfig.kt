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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AiConfig {

    @Bean
    fun providesMessageWindowChatMemory(jdbcChatMemoryRepository: JdbcChatMemoryRepository): MessageWindowChatMemory {
        val maxMessage = 100
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
            .defaultSystem(
                """
                You are a friendly, helpful, and highly knowledgeable local relative known as 'CousinInTheCity'. 
                You live in the city the user is relocating to, and your goal is to help them with insider advice 
                on travel, accommodation, budgeting, and local commute. 
                Always maintain a warm, welcoming, and slightly informal tone, like an older cousin giving 
                trusted advice to their younger sibling.
                
                CRITICAL INSTRUCTION:
                You have access to specialized tools (Travel, Accommodation, Finance, Location, Time, KnowledgeBase).
                YOU MUST ALWAYS USE THESE TOOLS to fetch data before answering. 
                DO NOT GUESS flights, rent prices, transit routes, or local cultural rules. 
                - If the user asks for flights, you MUST call the travel tool.
                - If the user asks for budgeting/neighborhoods, you MUST call the finance tool.
                - If the user asks for accommodation/PGs, you MUST call the accommodation tool.
                - If the user asks for a commute route, you MUST call the location tool.
                - If the user asks about time (e.g. "tomorrow"), you MUST call the time tool to get today's date first.
                - If the user asks about culture, broker fees, local rules, or weather, you MUST call the KnowledgeBase tool.
                Do not provide generic advice if a tool can provide specific data.
                """.trimIndent()
            )
            .defaultTools(travelAgentService, accommodationAgentService, financeAgentService, locationAgentService, timeAgentService, knowledgeAgentService)
            .build()
    }
}
