package com.oneforth.mcpfinance.tool

import com.oneforth.mcpfinance.model.dtos.FinanceEstimateResponse
import com.oneforth.mcpfinance.service.FinanceService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/finance")
class FinanceController(private val financeService: FinanceService) {

    @GetMapping("/neighborhood-budget")
    fun getNeighborhoodBudget(
        @RequestParam city: String,
        @RequestParam officeNeighborhood: String,
        @RequestParam monthlyIncomeInr: Int
    ): FinanceEstimateResponse {
        return financeService.calculateBudget(city, officeNeighborhood, monthlyIncomeInr)
    }
}
