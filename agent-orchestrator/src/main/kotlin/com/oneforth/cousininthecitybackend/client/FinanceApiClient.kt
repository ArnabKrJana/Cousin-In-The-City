package com.oneforth.cousininthecitybackend.client

import com.oneforth.cousininthecitybackend.model.dtos.FinanceEstimateResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface FinanceApiClient {
    @GetExchange("/api/finance/neighborhood-budget")
    @Cacheable("finance")
    fun getNeighborhoodBudget(
        @RequestParam("city") city: String,
        @RequestParam("officeNeighborhood") officeNeighborhood: String,
        @RequestParam("monthlyIncomeInr") monthlyIncomeInr: Int
    ): FinanceEstimateResponse
}
