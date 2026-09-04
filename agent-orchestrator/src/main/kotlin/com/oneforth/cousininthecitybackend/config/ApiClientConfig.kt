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

    @Bean
    fun financeApiClient(): com.oneforth.cousininthecitybackend.client.FinanceApiClient {
        val restClient = RestClient.builder().baseUrl("http://localhost:8083").build()
        val adaptor = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adaptor).build()
        return factory.createClient(com.oneforth.cousininthecitybackend.client.FinanceApiClient::class.java)
    }

    @Bean
    fun locationApiClient(): com.oneforth.cousininthecitybackend.client.LocationApiClient {
        val restClient = RestClient.builder().baseUrl("http://localhost:8084").build()
        val adaptor = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adaptor).build()
        return factory.createClient(com.oneforth.cousininthecitybackend.client.LocationApiClient::class.java)
    }
}
