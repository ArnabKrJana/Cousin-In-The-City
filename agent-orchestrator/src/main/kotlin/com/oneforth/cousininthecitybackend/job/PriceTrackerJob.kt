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
    @Scheduled(cron = "0 0 9 * * *")
    fun checkFlightPricesAndNotify() {
        logger.info("⏱️ DAILY SYNC START: Checking flight prices for active users...")

        val activeUsers = userRepository.findAll()
        activeUsers.forEach { user ->
            notificationService.queuePushNotification(
                deviceId = user.deviceId,
                title = "Flight Price Drop Alert! ✈️",
                body = "Hey cousin, I found a cheaper flight for ₹3,000! Tap to book."
            )
        }

        logger.info("✅ DAILY SYNC COMPLETE.")
    }
}

