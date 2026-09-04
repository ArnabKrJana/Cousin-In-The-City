package com.oneforth.cousininthecitybackend.service

import com.oneforth.cousininthecitybackend.client.FinanceApiClient
import com.oneforth.cousininthecitybackend.model.dtos.FinanceEstimateResponse
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service

@Service
class FinanceAgentService(private val client: FinanceApiClient) {

    @Tool(description = "Calculate living and commute budget across different neighborhood tiers to find affordable areas.")
    fun getNeighborhoodBudget(city: String, officeNeighborhood: String, monthlyIncomeInr: Int): FinanceEstimateResponse? {
        return try {
            client.getNeighborhoodBudget(city, officeNeighborhood, monthlyIncomeInr)
        } catch (e: Exception) {
            null
        }
    }
}
