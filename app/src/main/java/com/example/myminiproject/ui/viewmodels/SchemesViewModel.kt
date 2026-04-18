package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.FarmerInfo
import com.example.myminiproject.data.api.dto.SchemeEligibility
import com.example.myminiproject.data.api.dto.SchemeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Wrapper for UI state
sealed class SchemesUiState {
    object Loading : SchemesUiState()
    data class Success(
        val schemes: List<SchemeEligibility>,
        val summary: String,
        val categories: List<String> = listOf("All")
    ) : SchemesUiState()
    data class Error(val message: String) : SchemesUiState()
}

class SchemesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<SchemesUiState>(SchemesUiState.Loading)
    val uiState: StateFlow<SchemesUiState> = _uiState.asStateFlow()

    init {
        fetchSchemes()
    }

    fun fetchSchemes() {
        _uiState.value = SchemesUiState.Loading
        viewModelScope.launch {
            try {
                // In a real app, you would get this from UserRepository or session
                val mockFarmerInfo = FarmerInfo(
                    name = "Ramesh Kumar",
                    state = "Maharashtra",
                    landHoldingHectares = 2.5,
                    cropsGrown = listOf("Wheat", "Soybean"),
                    incomeLevel = "low"
                )

                val request = SchemeRequest(
                    farmerInfo = mockFarmerInfo,
                    language = "english"
                )

                val response = ApiClient.apiService.getEligibleSchemes(request)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    _uiState.value = SchemesUiState.Success(
                        schemes = body.eligibleSchemes,
                        summary = body.summary
                    )
                } else {
                    _uiState.value = SchemesUiState.Error("Server returned an error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = SchemesUiState.Error("Connection error: ${e.message}")
            }
        }
    }
}
