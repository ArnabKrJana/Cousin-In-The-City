package com.oneforth.cousininthecitybackend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ai.chat.client.ChatClient
import com.oneforth.cousininthecitybackend.model.dtos.AgentResponse

@SpringBootTest
class GeminiTest {

    @Autowired
    lateinit var chatClient: ChatClient

    @Test
    fun testGemini() {
        try {
            val response = chatClient.prompt()
                .user("Hello")
                .call()
                .entity(AgentResponse::class.java)
            println("=== SUCCESS ===")
            println(response)
        } catch (e: Exception) {
            println("=== EXCEPTION CAUGHT ===")
            e.printStackTrace()
        }
    }
}
