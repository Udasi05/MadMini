package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.TransactionRequest
import com.example.myminiproject.data.api.dto.TransactionResponse
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

class TransactionViewModel : ViewModel() {
    private val apiService = ApiClient.apiService
    private val auth = FirebaseAuth.getInstance()

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
            try {
                println("Loading transactions for user: $userId")
                println("API Base URL: ${ApiClient.apiService}")
                
                val response = apiService.getTransactions(userId)
                println("API Response: ${response.code()} - ${response.message()}")
                println("Response Headers: ${response.headers()}")
                
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()!!
                    println("Transactions loaded successfully: ${data.transactions.size} transactions")
                    println("Stats - Income: ${data.totalIncome}, Expense: ${data.totalExpense}, Balance: ${data.balance}")
                    
                    _transactions.value = data.transactions
                    _stats.value = TransactionStats(
                        totalIncome = data.totalIncome,
                        totalExpense = data.totalExpense,
                        balance = data.balance
                    )
                    _transactionState.value = TransactionState.Idle
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "API Error ${response.code()}: ${response.message()}"
                    println(errorMsg)
                    println("Error Body: $errorBody")
                    _transactionState.value = TransactionState.Error(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Network error: ${e.message}"
                println("Exception: $errorMsg")
                e.printStackTrace()
                _transactionState.value = TransactionState.Error(errorMsg)
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
                
                // Format current date/time
                val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

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
                    _transactionState.value = TransactionState.Success("Transaction added successfully!")
                    // Reload transactions to get updated list
                    loadTransactions()
                } else {
                    _transactionState.value = TransactionState.Error("Failed to add transaction: ${response.message()}")
                }
            } catch (e: Exception) {
                _transactionState.value = TransactionState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _transactionState.value = TransactionState.Idle
    }
}
