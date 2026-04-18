package com.example.myminiproject.ui.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.repository.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class CodeSent(val verificationId: String) : AuthUiState()
    data class VerificationSuccess(val userId: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun sendOtp(phone: String, activity: Activity) {
        _uiState.value = AuthUiState.Loading

        val callbacks =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Auto-sms retrieval or Instant validation
                        signInWithCredential(credential)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        _uiState.value = AuthUiState.Error(e.message ?: "Verification failed")
                    }

                    override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        _uiState.value = AuthUiState.CodeSent(verificationId)
                    }
                }

        authRepository.sendOtp(phone, activity, callbacks)
    }

    fun verifyOtp(verificationId: String, code: String, name: String = "", mode: String = "") {
        _uiState.value = AuthUiState.Loading
        try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithCredential(credential, name, mode)
        } catch (e: Exception) {
            _uiState.value = AuthUiState.Error("Invalid OTP Code.")
        }
    }

    /** Handle Google Sign-In authentication */
    fun signInWithGoogle(account: GoogleSignInAccount) {
        _uiState.value = AuthUiState.Loading
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(account)
            result
                    .onSuccess { userId ->
                        // Update user profile with Google account info
                        val displayName = account.displayName ?: "User"
                        val photoUrl = account.photoUrl?.toString()

                        val updateResult = authRepository.updateUserProfile(displayName, photoUrl)
                        updateResult
                                .onSuccess {
                                    _uiState.value = AuthUiState.VerificationSuccess(userId)
                                }
                                .onFailure {
                                    // Even if profile update fails, login succeeded
                                    _uiState.value = AuthUiState.VerificationSuccess(userId)
                                }
                    }
                    .onFailure {
                        _uiState.value = AuthUiState.Error(it.message ?: "Google Sign-In Failed")
                    }
        }
    }

    private fun signInWithCredential(
            credential: PhoneAuthCredential,
            name: String = "",
            mode: String = ""
    ) {
        viewModelScope.launch {
            val result = authRepository.signInWithCredential(credential)
            result
                    .onSuccess {
                        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                        if (mode == "signup" && name.isNotBlank() && user != null) {
                            val profileUpdates =
                                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build()
                            try {
                                user.updateProfile(profileUpdates).await()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        _uiState.value = AuthUiState.VerificationSuccess(it)
                    }
                    .onFailure { _uiState.value = AuthUiState.Error(it.message ?: "SignIn Failed") }
        }
    }
}
