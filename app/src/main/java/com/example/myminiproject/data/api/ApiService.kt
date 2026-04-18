package com.example.myminiproject.data.api

import com.example.myminiproject.data.api.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("schemes")
    suspend fun getEligibleSchemes(@Body request: SchemeRequest): Response<SchemesResponse>

    @POST("farm-help")
    suspend fun getFarmHelp(@Body request: FarmHelpRequest): Response<FarmHelpResponse>

    @POST("seasonal-income")
    suspend fun getSeasonalIncome(@Body request: SeasonalIncomeRequest): Response<SeasonalIncomeResponse>

    @POST("chat")
    suspend fun chatWithBot(@Body request: ChatRequest): Response<ChatResponse>

    @POST("profile")
    suspend fun updateProfile(@Body request: UserProfileRequest): Response<ProfileUpdateResponse>

    @GET("profile/{user_id}")
    suspend fun getProfile(@Path("user_id") userId: String): Response<UserProfileResponse>

    @POST("transactions")
    suspend fun addTransaction(@Body request: TransactionRequest): Response<TransactionAddResponse>

    @GET("transactions/{user_id}")
    suspend fun getTransactions(@Path("user_id") userId: String): Response<TransactionsListResponse>

    @POST("parse-voice-transaction")
    suspend fun parseVoiceTransaction(@Body request: VoiceTransactionRequest): Response<ParsedTransaction>

    @POST("tickets")
    suspend fun createTicket(@Body request: CreateTicketRequest): Response<CreateTicketResponse>

    @GET("tickets/{user_id}")
    suspend fun getUserTickets(@Path("user_id") userId: String): Response<TicketListResponse>
}
