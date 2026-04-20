package com.example.myminiproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.DhanSathiApplication
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.CreateTicketRequest
import com.example.myminiproject.data.api.dto.SupportTicket
import com.example.myminiproject.data.local.dao.SupportTicketDao
import com.example.myminiproject.data.local.entities.SupportTicketEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class TicketViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiClient.apiService
    private val ticketDao: SupportTicketDao = DhanSathiApplication.getDatabase(application).supportTicketDao()

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
                val localId = UUID.randomUUID().toString()

                // Step 1: Save to Room immediately
                val entity = SupportTicketEntity(
                    id = localId,
                    userId = userId,
                    subject = subject,
                    description = description,
                    category = category,
                    status = "open",
                    priority = "medium",
                    isSynced = false
                )
                ticketDao.insert(entity)
                println("📦 Ticket saved to Room DB")

                // Step 2: Try to sync to API
                try {
                    val request = CreateTicketRequest(
                        userId = userId,
                        subject = subject,
                        description = description,
                        category = category
                    )
                    val response = apiService.createTicket(request)
                    if (response.isSuccessful) {
                        ticketDao.markAsSynced(localId)
                        println("☁️ Ticket synced to API")
                    }
                } catch (e: Exception) {
                    println("API sync failed, will retry later: ${e.message}")
                }

                _submitSuccess.value = true
                getUserTickets(userId)
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getUserTickets(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Step 1: Load from Room first
            try {
                val localTickets = ticketDao.getTicketsByUserOnce(userId)
                if (localTickets.isNotEmpty()) {
                    _tickets.value = localTickets.map { it.toDto() }
                    println("📦 Loaded ${localTickets.size} tickets from Room DB")
                }
            } catch (e: Exception) {
                println("Room read error: ${e.message}")
            }

            // Step 2: Sync from API
            try {
                val response = apiService.getUserTickets(userId)
                if (response.isSuccessful) {
                    response.body()?.let { ticketListResponse ->
                        _tickets.value = ticketListResponse.tickets

                        // Cache to Room
                        val entities = ticketListResponse.tickets.map { it.toEntity() }
                        ticketDao.deleteAllByUser(userId)
                        ticketDao.insertAll(entities)
                        println("☁️ Synced ${entities.size} tickets from API → Room")
                    }
                }
            } catch (e: Exception) {
                println("API sync failed (offline mode): ${e.message}")
                if (_tickets.value.isEmpty()) {
                    _errorMessage.value = "No internet. No cached tickets."
                }
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

    // Conversion helpers
    private fun SupportTicketEntity.toDto(): SupportTicket {
        return SupportTicket(
            id = id,
            userId = userId,
            userName = userName,
            userPhone = userPhone,
            subject = subject,
            description = description,
            status = status,
            priority = priority,
            category = category,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    private fun SupportTicket.toEntity(): SupportTicketEntity {
        return SupportTicketEntity(
            id = id,
            userId = userId,
            userName = userName,
            userPhone = userPhone,
            subject = subject,
            description = description,
            status = status,
            priority = priority,
            category = category,
            createdAt = createdAt,
            updatedAt = updatedAt,
            isSynced = true
        )
    }
}