package com.oneforth.cousininthecitybackend.repository

import com.oneforth.cousininthecitybackend.model.entity.ChatThread
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatThreadRepository : JpaRepository<ChatThread, String> {
    fun findByDeviceIdOrderByUpdatedAtDesc(deviceId: String): List<ChatThread>
}
