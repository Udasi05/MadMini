package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.CreateTicketRequest
import com.example.myminiproject.data.api.dto.SupportTicket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketViewModel : ViewModel() {
    private val apiService = ApiClient.apiService

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _tickets = MutableStateFlow<List<SupportTicket>>(emptyList())
    val tickets: StateFlow<List<SupportTicket>> = _tickets.asStateFlow()

    private val _submitSuccess = MutableStateFlow(false)
    val submitSuccess: StateFlow<Boolean> = _submitSuccess.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun createTicket(userId: String, subject: String, description: String, category: String = "general") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val request = CreateTicketRequest(
                    userId = userId,
                    subject = subject,
                    description = description,
                    category = category
                )
                
                val response = apiService.createTicket(request)
                
                if (response.isSuccessful) {
                    _submitSuccess.value = true
                    // Refresh tickets after successful creation
                    getUserTickets(userId)
                } else {
                    _errorMessage.value = "Failed to submit ticket: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserTickets(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            
            try {
                val response = apiService.getUserTickets(userId)
                
                if (response.isSuccessful) {
                    response.body()?.let { ticketListResponse ->
                        _tickets.value = ticketListResponse.tickets
                    }
                } else {
                    _errorMessage.value = "Failed to load tickets: ${response.message()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearSubmitSuccess() {
        _submitSuccess.value = false
    }

    fun clearError() {
        _errorMessage.value = null
    }
}