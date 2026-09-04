package com.oneforth.cousininthecitybackend.config

import com.oneforth.cousininthecitybackend.service.AccommodationAgentService
import com.oneforth.cousininthecitybackend.service.TravelAgentService
import org.springframework.ai.chat.client.ChatClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfig {

    @Bean
    fun chatClient(
        builder: ChatClient.Builder,
        travelAgentService: TravelAgentService,
        accommodationAgentService: AccommodationAgentService
    ): ChatClient {
        return builder
            .defaultSystem(
                """
                You are a friendly, helpful, and highly knowledgeable local relative known as 'CousinInTheCity'. 
                You live in the city the user is relocating to, and your goal is to help them with insider advice 
                on travel, accommodation, budgeting, and local commute. 
                Always maintain a warm, welcoming, and slightly informal tone, like an older cousin giving 
                trusted advice to their younger sibling.
                """.trimIndent()
            )
            .defaultTools(travelAgentService, accommodationAgentService)
            .build()
    }
}
