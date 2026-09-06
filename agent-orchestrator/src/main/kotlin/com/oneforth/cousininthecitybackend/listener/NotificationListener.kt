package com.oneforth.cousininthecitybackend.listener

import com.oneforth.cousininthecitybackend.config.RabbitMqConfig
import com.oneforth.cousininthecitybackend.model.dtos.NotificationMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class NotificationListener {
    private val logger = LoggerFactory.getLogger(NotificationListener::class.java)

    @RabbitListener(queues = [RabbitMqConfig.MAIN_QUEUE])
    fun handleNotificationTask(message: NotificationMessage) {
        logger.info("De-queued Task! Sending FCM Push Notification to device ${message.deviceId}...")
        logger.info("Notification Title: ${message.title}")
        logger.info("Notification Body: ${message.body}")
        
        // TODO: In Phase 6, initialize FirebaseAdminSDK and call FirebaseMessaging.getInstance().send(fcmMessage)
        logger.info("FCM Notification sent successfully.")
    }

    @RabbitListener(queues = [RabbitMqConfig.DLQ_QUEUE])
    fun handleDeadLetterMessage(message: NotificationMessage) {
        logger.error("Failed to send FCM Notification to ${message.deviceId} after retries. Moved to Dead Letter Queue.")
    }
}
