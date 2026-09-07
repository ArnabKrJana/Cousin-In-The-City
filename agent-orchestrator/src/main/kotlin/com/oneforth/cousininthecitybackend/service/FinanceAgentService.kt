package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.FinanceApiClient
import com.oneforth.cousininthecitybackend.model.dtos.FinanceEstimateResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class FinanceAgentService(private val client: FinanceApiClient) {
    private val logger = LoggerFactory.getLogger(FinanceAgentService::class.java)

    @Tool(description = "Calculate living and commute budget across different neighborhood tiers to find affordable areas.")
    @CircuitBreaker(name = "financeService", fallbackMethod = "fallbackFinance")
    @Retry(name = "financeService", fallbackMethod = "fallbackFinance")
    fun getNeighborhoodBudget(city: String, officeNeighborhood: String, monthlyIncomeInr: Int): FinanceEstimateResponse? {
        return client.getNeighborhoodBudget(city, officeNeighborhood, monthlyIncomeInr)
    }

    fun fallbackFinance(city: String, officeNeighborhood: String, monthlyIncomeInr: Int, t: Throwable): FinanceEstimateResponse? {
        logger.error("Finance Service failed or circuit open. Returning null. Error: ${t.message}")
        return null
    }
}
