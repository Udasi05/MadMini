package com.example.myminiproject.data.api.dto

import com.google.gson.annotations.SerializedName

data class FarmerInfo(
    val name: String,
    val state: String,
    @SerializedName("land_holding_hectares") val landHoldingHectares: Double,
    @SerializedName("crops_grown") val cropsGrown: List<String>,
    @SerializedName("income_level") val incomeLevel: String = "low",
    val age: Int? = null
)

data class SchemeRequest(
    @SerializedName("farmer_info") val farmerInfo: FarmerInfo,
    val language: String = "simple"
)

data class FarmHelpRequest(
    @SerializedName("farmer_info") val farmerInfo: FarmerInfo,
    @SerializedName("selected_crop") val selectedCrop: String? = null,
    @SerializedName("current_season") val currentSeason: String? = null,
    val language: String = "simple"
)

data class SeasonalIncomeRequest(
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("user_name") val userName: String? = null,
    val language: String = "simple"
)

data class ChatRequest(
    val message: String,
    val language: String = "english",
    val context: String? = null
)

data class UserProfileRequest(
    @SerializedName("user_id") val userId: String,
    val name: String = "",
    val phone: String = "",
    val state: String = "",
    @SerializedName("land_holding_hectares") val landHoldingHectares: Double = 0.0,
    @SerializedName("crops_grown") val cropsGrown: List<String> = emptyList(),
    @SerializedName("income_level") val incomeLevel: String = "low"
)

data class TransactionRequest(
    @SerializedName("user_id") val userId: String,
    val title: String,
    val amount: Double,
    val type: String, // "income" or "expense"
    val category: String,
    val icon: String = "₹",
    val date: String = ""
)

data class VoiceTransactionRequest(
    val text: String,
    val language: String = "english"
)

data class CreateTicketRequest(
    @SerializedName("user_id") val userId: String,
    val subject: String,
    val description: String,
    val category: String = "general"
)
