package com.example.myminiproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.DhanSathiApplication
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.TransactionRequest
import com.example.myminiproject.data.api.dto.TransactionResponse
import com.example.myminiproject.data.local.dao.TransactionDao
import com.example.myminiproject.data.local.entities.TransactionEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class TransactionState {
    object Idle : TransactionState()
    object Loading : TransactionState()
    data class Success(val message: String) : TransactionState()
    data class Error(val message: String) : TransactionState()
}

data class TransactionStats(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val balance: Double = 0.0
)

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiClient.apiService
    private val auth = FirebaseAuth.getInstance()
    private val transactionDao: TransactionDao = DhanSathiApplication.getDatabase(application).transactionDao()

    private val _transactionState = MutableStateFlow<TransactionState>(TransactionState.Idle)
    val transactionState: StateFlow<TransactionState> = _transactionState.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionResponse>>(emptyList())
    val transactions: StateFlow<List<TransactionResponse>> = _transactions.asStateFlow()

    private val _stats = MutableStateFlow(TransactionStats())
    val stats: StateFlow<TransactionStats> = _stats.asStateFlow()

    fun loadTransactions() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _transactionState.value = TransactionState.Loading

            // Step 1: Load from Room first (instant, offline)
            try {
                val localTransactions = transactionDao.getTransactionsByUserOnce(userId)
                if (localTransactions.isNotEmpty()) {
                    _transactions.value = localTransactions.map { it.toResponse() }
                    updateStats(localTransactions)
                    _transactionState.value = TransactionState.Idle
                    println("📦 Loaded ${localTransactions.size} transactions from Room DB")
                }
            } catch (e: Exception) {
                println("Room read error: ${e.message}")
            }

            // Step 2: Try to sync from API (background refresh)
            try {
                val response = apiService.getTransactions(userId)
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    _transactions.value = data.transactions
                    _stats.value = TransactionStats(
                        totalIncome = data.totalIncome,
                        totalExpense = data.totalExpense,
                        balance = data.balance
                    )

                    // Cache to Room
                    val entities = data.transactions.map { it.toEntity(userId) }
                    transactionDao.deleteAllByUser(userId)
                    transactionDao.insertAll(entities)
                    println("☁️ Synced ${entities.size} transactions from API → Room")

                    _transactionState.value = TransactionState.Idle
                }
            } catch (e: Exception) {
                // API failed but we already have local data — that's fine
                println("API sync failed (offline mode): ${e.message}")
                if (_transactions.value.isEmpty()) {
                    _transactionState.value = TransactionState.Error("No internet. No cached data available.")
                }
            }
        }
    }

    fun addTransaction(
        title: String,
        amount: String,
        type: String,
        category: String,
        icon: String
    ) {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _transactionState.value = TransactionState.Loading
            try {
                val amountValue = amount.toDoubleOrNull() ?: 0.0
                val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                val currentDate = dateFormat.format(Date())
                val localId = UUID.randomUUID().toString()

                // Step 1: Save to Room immediately (offline-first)
                val entity = TransactionEntity(
                    id = localId,
                    title = title,
                    amount = amountValue,
                    type = type,
                    category = category,
                    icon = icon,
                    date = currentDate,
                    userId = userId,
                    timestamp = System.currentTimeMillis(),
                    isSynced = false
                )
                transactionDao.insert(entity)
                println("📦 Transaction saved to Room DB (offline)")

                // Step 2: Try to sync to API
                try {
                    val request = TransactionRequest(
                        userId = userId,
                        title = title,
                        amount = amountValue,
                        type = type,
                        category = category,
                        icon = icon,
                        date = currentDate
                    )
                    val response = apiService.addTransaction(request)
                    if (response.isSuccessful && response.body() != null) {
                        transactionDao.markAsSynced(localId)
                        println("☁️ Transaction synced to API")
                    }
                } catch (e: Exception) {
                    println("API sync failed, will retry later: ${e.message}")
                }

                _transactionState.value = TransactionState.Success("Transaction added successfully!")
                loadTransactions()
            } catch (e: Exception) {
                _transactionState.value = TransactionState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _transactionState.value = TransactionState.Idle
    }

    private fun updateStats(transactions: List<TransactionEntity>) {
        val income = transactions.filter { it.type == "income" }.sumOf { it.amount }
        val expense = transactions.filter { it.type == "expense" }.sumOf { it.amount }
        _stats.value = TransactionStats(
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense
        )
    }

    // Extension functions to convert between API response and Room entity
    private fun TransactionEntity.toResponse(): TransactionResponse {
        return TransactionResponse(
            id = id,
            title = title,
            amount = amount,
            type = type,
            category = category,
            icon = icon,
            date = date,
            userId = userId,
            timestamp = timestamp
        )
    }

    private fun TransactionResponse.toEntity(userId: String): TransactionEntity {
        return TransactionEntity(
            id = id,
            title = title,
            amount = amount,
            type = type,
            category = category,
            icon = icon,
            date = date,
            userId = userId,
            timestamp = timestamp,
            isSynced = true
        )
    }
}
