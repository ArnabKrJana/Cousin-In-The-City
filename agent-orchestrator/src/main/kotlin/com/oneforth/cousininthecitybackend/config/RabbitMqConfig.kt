package com.oneforth.cousininthecitybackend.config

import org.springframework.amqp.core.*
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonObjectMapper

@Configuration
class RabbitMqConfig {

    companion object {
        const val EXCHANGE_NAME = "fcm.notification.exchange"
        const val MAIN_QUEUE = "fcm.notification.queue"
        const val ROUTING_KEY = "fcm.notification.routingKey"

        const val DLX_EXCHANGE = "fcm.notification.dlx"
        const val DLQ_QUEUE = "fcm.notification.dlq"
        const val DLQ_ROUTING_KEY = "fcm.notification.dlqRoutingKey"
    }

    @Bean
    fun jsonMessageConverter(): JacksonJsonMessageConverter {
        val jsonMapper = jacksonObjectMapper() as JsonMapper
        return JacksonJsonMessageConverter(jsonMapper)
    }

    // Dead Letter Infrastructure
    @Bean
    fun deadLetterExchange(): DirectExchange = DirectExchange(DLX_EXCHANGE)

    @Bean
    fun deadLetterQueue(): Queue = QueueBuilder.durable(DLQ_QUEUE).build()

    @Bean
    fun dlqBinding(): Binding = BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ_ROUTING_KEY)

    // Main Infrastructure
    @Bean
    fun mainExchange(): DirectExchange = DirectExchange(EXCHANGE_NAME)

    @Bean
    fun mainQueue(): Queue {
        return QueueBuilder.durable(MAIN_QUEUE)
            .deadLetterExchange(DLX_EXCHANGE)
            .deadLetterRoutingKey(DLQ_ROUTING_KEY)
            .build()
    }

    @Bean
    fun mainBinding(): Binding = BindingBuilder.bind(mainQueue()).to(mainExchange()).with(ROUTING_KEY)
}
