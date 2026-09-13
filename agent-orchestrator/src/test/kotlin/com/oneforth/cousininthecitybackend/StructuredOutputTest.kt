package com.oneforth.cousininthecitybackend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ai.chat.client.ChatClient
import com.oneforth.cousininthecitybackend.model.dtos.AgentResponse

@SpringBootTest
class StructuredOutputTest {

    @Autowired
    lateinit var chatClient: ChatClient

    @Test
    fun testStructuredOutput() {
        val response = chatClient.prompt()
            .user("I am moving to Mumbai. My office is in Bandra and salary is 40,000. Give me flights from Delhi, a PG, and commute info.")
            .call()
            .entity(AgentResponse::class.java)
        
        println("=== AgentResponse START ===")
        println("Message: " + response?.message)
        println("IntentType: " + response?.intentType)
        println("ActionData: " + response?.actionData)
        println("=== AgentResponse END ===")
    }
}
