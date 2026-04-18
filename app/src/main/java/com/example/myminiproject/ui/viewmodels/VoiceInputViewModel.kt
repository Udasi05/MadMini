package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.ParsedTransaction
import com.example.myminiproject.data.api.dto.TransactionRequest
import com.example.myminiproject.data.api.dto.VoiceTransactionRequest
import com.example.myminiproject.data.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class VoiceState {
    object Idle : VoiceState()
    object Recording : VoiceState()
    object Loading : VoiceState()
    data class Error(val message: String) : VoiceState()
    object TransactionSaved : VoiceState()
}

class VoiceInputViewModel : ViewModel() {
    private val apiService = ApiClient.apiService
    private val auth = FirebaseAuth.getInstance()

    private val _voiceState = MutableStateFlow<VoiceState>(VoiceState.Idle)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _parsedTransaction = MutableStateFlow<ParsedTransaction?>(null)
    val parsedTransaction: StateFlow<ParsedTransaction?> = _parsedTransaction.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    fun setLanguage(language: String) {
        _selectedLanguage.value = language
    }

    fun parseVoiceInput(text: String) {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _voiceState.value = VoiceState.Loading
            try {
                val languageCode = when (_selectedLanguage.value) {
                    "Hindi" -> "hindi"
                    "Marathi" -> "marathi"
                    else -> "english"
                }

                val request = VoiceTransactionRequest(
                    text = text,
                    language = languageCode
                )

                val response = apiService.parseVoiceTransaction(request)
                if (response.isSuccessful && response.body() != null) {
                    _parsedTransaction.value = response.body()
                    _voiceState.value = VoiceState.Idle
                } else {
                    _voiceState.value = VoiceState.Error("Failed to parse voice input: ${response.message()}")
                }
            } catch (e: Exception) {
                _voiceState.value = VoiceState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun saveTransaction() {
        val userId = auth.currentUser?.uid ?: return
        val parsed = _parsedTransaction.value ?: return

        viewModelScope.launch {
            _voiceState.value = VoiceState.Loading
            try {
                // Format current date/time
                val dateFormat = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
                val currentDate = dateFormat.format(Date())

                // Get icon for category
                val icon = Transaction.categoryIcons[parsed.category] 
                    ?: if (parsed.type == "income") "💰" else "💸"

                val request = TransactionRequest(
                    userId = userId,
                    title = parsed.description,
                    amount = parsed.amount,
                    type = parsed.type,
                    category = parsed.category,
                    icon = icon,
                    date = currentDate
                )

                val response = apiService.addTransaction(request)
                if (response.isSuccessful && response.body() != null) {
                    _voiceState.value = VoiceState.TransactionSaved
                } else {
                    _voiceState.value = VoiceState.Error("Failed to save transaction: ${response.message()}")
                }
            } catch (e: Exception) {
                _voiceState.value = VoiceState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun clearParsedTransaction() {
        _parsedTransaction.value = null
        _voiceState.value = VoiceState.Idle
    }

    fun resetState() {
        _voiceState.value = VoiceState.Idle
    }
}
