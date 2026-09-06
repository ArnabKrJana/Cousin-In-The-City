package com.oneforth.cousininthecitybackend.service

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TimeAgentService {

    @Tool(description = "Get the current real-time system date and time.")
    fun getCurrentDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return LocalDateTime.now().format(formatter)
    }
}
