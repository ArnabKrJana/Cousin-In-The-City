package com.oneforth.cousininthecitybackend

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ai.chat.client.ChatClient

@SpringBootTest
class CousinInTheCityBackendApplicationTests {

    @Autowired
    lateinit var chatClient: ChatClient

    @Test
    fun contextLoads() {
        val response = chatClient.prompt().user("I am moving to Mumbai. Find me a flight from Delhi to Mumbai tomorrow.").call().content()
        println("=== AI RESPONSE START ===")
        println(response)
        println("=== AI RESPONSE END ===")
    }

}
