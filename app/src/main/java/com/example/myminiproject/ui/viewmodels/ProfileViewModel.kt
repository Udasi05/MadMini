package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.UserProfileRequest
import com.example.myminiproject.data.api.dto.UserProfileResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(val message: String) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

data class ProfileData(
    val name: String = "",
    val state: String = "",
    val landHolding: Double = 0.0,
    val crops: List<String> = emptyList()
)

class ProfileViewModel : ViewModel() {
    private val apiService = ApiClient.apiService
    private val auth = FirebaseAuth.getInstance()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData.asStateFlow()

    fun loadProfile() {
        val userId = auth.currentUser?.uid ?: return
        
        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val response = apiService.getProfile(userId)
                if (response.isSuccessful && response.body() != null) {
                    val profile = response.body()!!
                    _profileData.value = ProfileData(
                        name = profile.name,
                        state = profile.state,
                        landHolding = profile.landHoldingHectares,
                        crops = profile.cropsGrown
                    )
                    _profileState.value = ProfileState.Idle
                } else {
                    _profileState.value = ProfileState.Error("Failed to load profile")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun updateProfile(name: String, state: String, landHolding: String, crops: String) {
        val userId = auth.currentUser?.uid ?: return
        val phone = auth.currentUser?.phoneNumber ?: ""

        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                // Parse land holding
                val landHoldingValue = landHolding.toDoubleOrNull() ?: 0.0

                // Parse crops (comma-separated)
                val cropsList = crops.split(",")
                    .map { it.trim() }
                    .filter { it.isNotBlank() }

                val request = UserProfileRequest(
                    userId = userId,
                    name = name,
                    phone = phone,
                    state = state,
                    landHoldingHectares = landHoldingValue,
                    cropsGrown = cropsList,
                    incomeLevel = "low" // Default for now
                )

                val response = apiService.updateProfile(request)
                if (response.isSuccessful && response.body() != null) {
                    // Update Firebase display name
                    auth.currentUser?.updateProfile(
                        com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                    )
                    
                    _profileState.value = ProfileState.Success("Profile updated successfully!")
                } else {
                    _profileState.value = ProfileState.Error("Failed to update profile: ${response.message()}")
                }
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}
