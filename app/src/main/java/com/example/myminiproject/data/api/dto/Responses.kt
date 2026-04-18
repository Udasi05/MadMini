package com.example.myminiproject.data.api.dto

import com.google.gson.annotations.SerializedName

// Schemes
data class SchemeEligibility(
    @SerializedName("scheme_name") val schemeName: String,
    val eligible: Boolean,
    val benefits: String,
    @SerializedName("documents_required") val documentsRequired: List<String>,
    @SerializedName("application_link") val applicationLink: String,
    val justification: String
)

data class SchemesResponse(
    @SerializedName("farmer_name") val farmerName: String,
    @SerializedName("eligible_schemes") val eligibleSchemes: List<SchemeEligibility>,
    val summary: String
)

// Farm Help
data class FarmHelp(
    val crop: String,
    val season: String,
    @SerializedName("yield_prediction") val yieldPrediction: String,
    @SerializedName("cost_estimation") val costEstimation: String,
    @SerializedName("price_prediction") val pricePrediction: String,
    @SerializedName("profit_estimation") val profitEstimation: String,
    @SerializedName("risk_analysis") val riskAnalysis: String,
    val roadmap: String
)

data class FarmHelpResponse(
    @SerializedName("farmer_name") val farmerName: String,
    @SerializedName("farm_help") val farmHelp: FarmHelp,
    @SerializedName("detailed_recommendations") val detailedRecommendations: String
)

// Seasonal Income
data class IncomeSeason(
    @SerializedName("source_type") val sourceType: String,
    @SerializedName("source_name") val sourceName: String,
    val months: List<Int>,
    @SerializedName("annual_amount") val annualAmount: Double,
    val variability: String
)

data class MonthlySavingsPlan(
    val month: String,
    @SerializedName("predicted_income") val predictedIncome: Double,
    @SerializedName("safe_spending_limit") val safeSpendingLimit: Double,
    @SerializedName("recommended_savings") val recommendedSavings: Double,
    @SerializedName("spending_ratio") val spendingRatio: Double,
    val notes: String
)

data class SeasonalIncomeResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    val state: String,
    @SerializedName("total_annual_income") val totalAnnualIncome: Double,
    @SerializedName("income_sources") val incomeSources: List<IncomeSeason>,
    @SerializedName("monthly_analysis") val monthlyAnalysis: List<MonthlySavingsPlan>,
    @SerializedName("seasonality_insights") val seasonalityInsights: String,
    @SerializedName("spending_recommendations") val spendingRecommendations: String,
    @SerializedName("savings_strategy") val savingsStrategy: String
)

// Chat
data class BilingualText(
    val english: String,
    val marathi: String
)

data class ChatResponse(
    @SerializedName("user_message") val userMessage: String,
    @SerializedName("bot_response") val botResponse: BilingualText,
    val suggestions: List<String>
)

// Profile
data class UserProfileResponse(
    @SerializedName("user_id") val userId: String,
    val name: String = "",
    val phone: String = "",
    val state: String = "",
    @SerializedName("land_holding_hectares") val landHoldingHectares: Double = 0.0,
    @SerializedName("crops_grown") val cropsGrown: List<String> = emptyList(),
    @SerializedName("income_level") val incomeLevel: String = "low"
)

data class ProfileUpdateResponse(
    val status: String,
    val message: String
)

data class TransactionResponse(
    val id: String,
    @SerializedName("user_id") val userId: String,
    val title: String,
    val amount: Double,
    val type: String,
    val category: String,
    val icon: String,
    val date: String,
    val timestamp: Long
)

data class TransactionsListResponse(
    val transactions: List<TransactionResponse>,
    @SerializedName("total_income") val totalIncome: Double,
    @SerializedName("total_expense") val totalExpense: Double,
    val balance: Double
)

data class TransactionAddResponse(
    val status: String,
    val message: String,
    @SerializedName("transaction_id") val transactionId: String
)

data class ParsedTransaction(
    val amount: Double,
    val type: String,
    val category: String,
    val description: String,
    val confidence: String
)

data class CreateTicketResponse(
    val status: String,
    val message: String,
    @SerializedName("ticket_id") val ticketId: String
)

data class TicketResponse(
    val id: String,
    val message: String,
    @SerializedName("is_from_support") val isFromSupport: Boolean = false,
    @SerializedName("author_name") val authorName: String,
    val timestamp: Long
)

data class SupportTicket(
    val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_phone") val userPhone: String,
    val subject: String,
    val description: String,
    val status: String,
    val priority: String,
    val category: String,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long,
    @SerializedName("assigned_to") val assignedTo: String = "",
    val responses: List<TicketResponse> = emptyList()
)

data class TicketListResponse(
    val tickets: List<SupportTicket>,
    @SerializedName("total_count") val totalCount: Int
)
