package com.oneforth.cousininthecitybackend.job

import com.oneforth.cousininthecitybackend.repository.AppUserRepository
import com.oneforth.cousininthecitybackend.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PriceTrackerJob(
    private val userRepository: AppUserRepository,
    private val notificationService: NotificationService
) {
    private val logger = LoggerFactory.getLogger(PriceTrackerJob::class.java)

    // Runs every 24 hours at 9:00 AM (0 0 9 * * *)
    @Scheduled(cron = "0 0 9 * * *")
    fun checkFlightPricesAndNotify() {
        logger.info("⏱️ DAILY SYNC START: Checking flight prices for active users...")

        val activeUsers = userRepository.findAll()
        activeUsers.forEach { user ->
            // In a real app, we would query mcp-travel here based on user's saved wishlist
            // For now, we queue a mock notification via RabbitMQ
            notificationService.queuePushNotification(
                deviceId = user.deviceId,
                title = "Flight Price Drop Alert! ✈️",
                body = "Hey cousin, I found a cheaper flight for ₹3,000! Tap to book."
            )
        }

        logger.info("✅ DAILY SYNC COMPLETE.")
    }
}
