package com.oneforth.cousininthecitybackend.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chat_threads")
class ChatThread(
    @Id
    val threadId: String = UUID.randomUUID().toString(),
    val deviceId: String,
    var title: String = "New Chat",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
