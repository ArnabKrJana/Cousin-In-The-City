package com.oneforth.mcpfinance.service

import com.oneforth.mcpfinance.model.dtos.FinanceEstimateResponse
import com.oneforth.mcpfinance.model.dtos.NeighborhoodTier
import org.springframework.stereotype.Service
import kotlin.math.abs

@Service
class FinanceService {

    // Mock Database of cities and their tiers
    private val cityData = mapOf(
        "MUMBAI" to mapOf(
            "Premium" to listOf("Bandra", "Juhu", "South Bombay"),
            "Standard" to listOf("Andheri", "Goregaon", "Malad"),
            "Affordable" to listOf("Thane", "Navi Mumbai", "Borivali")
        ),
        "BANGALORE" to mapOf(
            "Premium" to listOf("Koramangala", "Indiranagar", "CBD"),
            "Standard" to listOf("Whitefield", "HSR Layout", "Marathahalli"),
            "Affordable" to listOf("Electronic City", "Yelahanka", "Kengeri")
        ),
        "DELHI" to mapOf(
            "Premium" to listOf("South Extension", "Vasant Vihar", "Def Col"),
            "Standard" to listOf("Lajpat Nagar", "Hauz Khas", "Rajouri Garden"),
            "Affordable" to listOf("Rohini", "Dwarka", "Uttam Nagar")
        )
    )

    private val baseRent = mapOf(
        "Premium" to 45000,
        "Standard" to 25000,
        "Affordable" to 12000
    )

    fun calculateBudget(city: String, officeNeighborhood: String, monthlyIncomeInr: Int): FinanceEstimateResponse {
        val normalizedCity = city.uppercase()
        val neighborhoods = cityData[normalizedCity] ?: cityData["BANGALORE"]!! // Default to Bangalore if unknown

        // Determine which tier the office is in (default to Standard)
        var officeTierIndex = 1 
        if (neighborhoods["Premium"]?.any { it.equals(officeNeighborhood, ignoreCase = true) } == true) officeTierIndex = 0
        if (neighborhoods["Affordable"]?.any { it.equals(officeNeighborhood, ignoreCase = true) } == true) officeTierIndex = 2

        val tiers = mutableListOf<NeighborhoodTier>()

        val tierNames = listOf("Premium", "Standard", "Affordable")
        
        for ((index, tierName) in tierNames.withIndex()) {
            val areas = neighborhoods[tierName] ?: emptyList()
            
            // Rent Calculation
            // Add a small city multiplier (Mumbai +20%)
            val cityMultiplier = if (normalizedCity == "MUMBAI") 1.2 else 1.0
            val estimatedRent = (baseRent[tierName]!! * cityMultiplier).toInt()

            // Commute Trade-off Analyzer
            // The further the housing tier is from the office tier, the higher the commute cost
            val distanceFactor = abs(officeTierIndex - index)
            val baseCommuteCost = 2000
            val estimatedCommuteCost = baseCommuteCost + (distanceFactor * 4000)

            // Other living costs (food, utilities) usually scale slightly with the tier
            val foodAndUtilities = 10000 + ((2 - index) * 3000)
            
            val totalCost = estimatedRent + estimatedCommuteCost + foodAndUtilities

            // Affordability Status
            val status = when {
                totalCost > monthlyIncomeInr -> "Over Budget"
                (monthlyIncomeInr - totalCost) < 5000 -> "Tight"
                else -> "Comfortable"
            }

            tiers.add(
                NeighborhoodTier(
                    tierName = tierName,
                    exampleAreas = areas,
                    estimatedRentInr = estimatedRent,
                    estimatedCommuteCostInr = estimatedCommuteCost,
                    totalEstimatedLivingCostInr = totalCost,
                    affordabilityStatus = status
                )
            )
        }

        return FinanceEstimateResponse(
            city = city,
            officeNeighborhood = officeNeighborhood,
            monthlyIncomeInr = monthlyIncomeInr,
            tiers = tiers
        )
    }
}
