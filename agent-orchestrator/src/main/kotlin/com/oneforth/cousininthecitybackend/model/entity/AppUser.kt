package com.oneforth.cousininthecitybackend.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "app_users")
class AppUser(
    @Id
    val deviceId: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
