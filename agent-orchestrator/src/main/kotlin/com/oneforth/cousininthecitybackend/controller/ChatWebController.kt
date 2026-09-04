package com.oneforth.cousininthecitybackend.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ChatWebController {

    @GetMapping("/")
    fun chatPage(): String {
        return "chat"
    }
}
