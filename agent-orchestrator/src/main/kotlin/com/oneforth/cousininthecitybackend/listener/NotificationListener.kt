package com.oneforth.cousininthecitybackend.listener

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.oneforth.cousininthecitybackend.config.RabbitMqConfig
import com.oneforth.cousininthecitybackend.model.dtos.NotificationMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import com.oneforth.cousininthecitybackend.repository.AppUserRepository

@Component
class NotificationListener(
    private val userRepository: AppUserRepository
) {
    private val logger = LoggerFactory.getLogger(NotificationListener::class.java)

    @RabbitListener(queues = [RabbitMqConfig.MAIN_QUEUE])
    fun handleNotificationTask(message: NotificationMessage) {
        logger.info("De-queued Task! Sending FCM Push Notification to device ${message.deviceId}...")
        
        try {
            val user = userRepository.findById(message.deviceId).orElse(null)
            val fcmToken = user?.fcmToken
            
            if (fcmToken.isNullOrBlank()) {
                logger.warn("Cannot send push notification. User  does not have an FCM token registered.")
                return // Don't throw exception, just drop it since we can't do anything without a token
            }
            
            val fcmNotification = Notification.builder()
                .setTitle(message.title)
                .setBody(message.body)
                .build()

            val fcmMessage = Message.builder()
                .setToken(fcmToken) // The actual FCM registration token from the DB
                .setNotification(fcmNotification)
                .putData("intentType", "NOTIFICATION") // Custom data payload
                .build()

            val response = FirebaseMessaging.getInstance().send(fcmMessage)
            logger.info("FCM Notification sent successfully. Message ID: $response")
            
        } catch (e: Exception) {
            logger.error("Failed to send FCM message: ${e.message}")
            throw e // Throwing exceptions triggers RabbitMQ to retry, and eventually DLQ
        }
    }

    @RabbitListener(queues = [RabbitMqConfig.DLQ_QUEUE])
    fun handleDeadLetterMessage(message: NotificationMessage) {
        logger.error("Failed to send FCM Notification to ${message.deviceId} after retries. Moved to Dead Letter Queue.")
    }
}


