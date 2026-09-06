package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.config.RabbitMqConfig
import com.oneforth.cousininthecitybackend.model.dtos.NotificationMessage
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val rabbitTemplate: RabbitTemplate
) {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun queuePushNotification(deviceId: String, title: String, body: String) {
        val message = NotificationMessage(deviceId, title, body)
        logger.info("Queuing FCM Notification to RabbitMQ for device: $deviceId")
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, message)
    }
}
