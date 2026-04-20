package com.example.myminiproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.DhanSathiApplication
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.UserProfileRequest
import com.example.myminiproject.data.local.dao.UserProfileDao
import com.example.myminiproject.data.local.entities.UserProfileEntity
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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiClient.apiService
    private val auth = FirebaseAuth.getInstance()
    private val userProfileDao: UserProfileDao = DhanSathiApplication.getDatabase(application).userProfileDao()

    private val _profileState = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData: StateFlow<ProfileData?> = _profileData.asStateFlow()

    fun loadProfile() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            _profileState.value = ProfileState.Loading

            // Step 1: Load from Room first (instant)
            try {
                val localProfile = userProfileDao.getProfileByUid(userId)
                if (localProfile != null) {
                    _profileData.value = ProfileData(
                        name = localProfile.name,
                        state = localProfile.state,
                        landHolding = localProfile.landHoldingHectares,
                        crops = localProfile.cropsGrown.split(",").filter { it.isNotBlank() }
                    )
                    _profileState.value = ProfileState.Idle
                    println("📦 Profile loaded from Room DB")
                }
            } catch (e: Exception) {
                println("Room read error: ${e.message}")
            }

            // Step 2: Sync from API
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

                    // Cache to Room
                    val entity = UserProfileEntity(
                        uid = userId,
                        name = profile.name,
                        phone = profile.phone,
                        state = profile.state,
                        landHoldingHectares = profile.landHoldingHectares,
                        cropsGrown = profile.cropsGrown.joinToString(","),
                        incomeLevel = profile.incomeLevel,
                        isSynced = true
                    )
                    userProfileDao.insert(entity)
                    println("☁️ Profile synced from API → Room")

                    _profileState.value = ProfileState.Idle
                }
            } catch (e: Exception) {
                println("API sync failed (offline mode): ${e.message}")
                if (_profileData.value == null) {
                    _profileState.value = ProfileState.Error("No internet. No cached profile.")
                }
            }
        }
    }

    fun updateProfile(name: String, state: String, landHolding: String, crops: String) {
        val userId = auth.currentUser?.uid ?: return
        val phone = auth.currentUser?.phoneNumber ?: ""

        viewModelScope.launch {
            _profileState.value = ProfileState.Loading
            try {
                val landHoldingValue = landHolding.toDoubleOrNull() ?: 0.0
                val cropsList = crops.split(",").map { it.trim() }.filter { it.isNotBlank() }

                // Step 1: Save to Room immediately
                userProfileDao.updateProfileFields(
                    uid = userId,
                    name = name,
                    state = state,
                    landHolding = landHoldingValue,
                    crops = cropsList.joinToString(",")
                )
                println("📦 Profile saved to Room DB")

                // Step 2: Try to sync to API
                try {
                    val request = UserProfileRequest(
                        userId = userId,
                        name = name,
                        phone = phone,
                        state = state,
                        landHoldingHectares = landHoldingValue,
                        cropsGrown = cropsList,
                        incomeLevel = "low"
                    )

                    val response = apiService.updateProfile(request)
                    if (response.isSuccessful && response.body() != null) {
                        auth.currentUser?.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                        )
                        userProfileDao.markAsSynced(userId)
                        println("☁️ Profile synced to API")
                    }
                } catch (e: Exception) {
                    println("API sync failed, will retry later: ${e.message}")
                }

                _profileState.value = ProfileState.Success("Profile updated successfully!")
            } catch (e: Exception) {
                _profileState.value = ProfileState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetState() {
        _profileState.value = ProfileState.Idle
    }
}
