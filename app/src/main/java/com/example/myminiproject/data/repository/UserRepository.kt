package com.example.myminiproject.data.repository

import com.example.myminiproject.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String = auth.currentUser?.uid ?: "demo_user"

    private fun userDoc() = db.collection("users").document(getUserId())

    suspend fun createOrUpdateProfile(profile: UserProfile): Result<Unit> {
        return try {
            userDoc().set(profile.copy(uid = getUserId())).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProfile(): Result<UserProfile> {
        return try {
            val doc = userDoc().get().await()
            val profile = doc.toObject(UserProfile::class.java)
            Result.success(profile ?: UserProfile(
                uid = getUserId(),
                name = "Ramesh Kumar",
                phone = auth.currentUser?.phoneNumber ?: "+91 98XX XXX X23"
            ))
        } catch (e: Exception) {
            Result.success(UserProfile(
                uid = getUserId(),
                name = "Ramesh Kumar",
                phone = "+91 98XX XXX X23"
            ))
        }
    }

    suspend fun updateLanguage(language: String): Result<Unit> {
        return try {
            userDoc().update("language", language).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotifications(enabled: Boolean): Result<Unit> {
        return try {
            userDoc().update("notificationsEnabled", enabled).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
