package com.oneforth.cousininthecitybackend.model.dtos

data class NeighborhoodTier(
    val tierName: String,
    val exampleAreas: List<String>,
    val estimatedRentInr: Int,
    val estimatedCommuteCostInr: Int,
    val totalEstimatedLivingCostInr: Int,
    val affordabilityStatus: String
)

data class FinanceEstimateResponse(
    val city: String,
    val officeNeighborhood: String,
    val monthlyIncomeInr: Int,
    val tiers: List<NeighborhoodTier>
)
