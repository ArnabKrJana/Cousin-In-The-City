package com.oneforth.cousininthecitybackend.config

import com.oneforth.cousininthecitybackend.client.AccommodationApiClient
import com.oneforth.cousininthecitybackend.client.TravelApiClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class ApiClientConfig {

    @Bean
    fun travelApiClient(): TravelApiClient {
        val restClient = RestClient.builder().baseUrl("http://localhost:8081").build()
        val adaptor = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adaptor).build()
        return factory.createClient(TravelApiClient::class.java)
    }

    @Bean
    fun accommodationApiClient(): AccommodationApiClient {
        val restClient = RestClient.builder().baseUrl("http://localhost:8082").build()
        val adaptor = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adaptor).build()
        return factory.createClient(AccommodationApiClient::class.java)
    }
}
