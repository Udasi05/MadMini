package com.example.myminiproject.data.repository

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()

    val currentUser
        get() = auth.currentUser
    val isLoggedIn
        get() = auth.currentUser != null

    fun sendOtp(
            phone: String,
            activity: android.app.Activity,
            callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber("+91$phone")
                        .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    suspend fun signInWithCredential(credential: PhoneAuthCredential): Result<String> {
        return try {
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Sign in with Google using the account obtained from GoogleSignInClient */
    suspend fun signInWithGoogle(account: GoogleSignInAccount): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Update user profile with display name and profile picture */
    suspend fun updateUserProfile(displayName: String, photoUrl: String? = null): Result<Unit> {
        return try {
            val user = auth.currentUser ?: throw Exception("No authenticated user")

            val profileUpdates =
                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)

            if (photoUrl != null) {
                profileUpdates.setPhotoUri(android.net.Uri.parse(photoUrl))
            }

            user.updateProfile(profileUpdates.build()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
