package com.example.myminiproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val state: String = "",
    val landHoldingHectares: Double = 0.0,
    val cropsGrown: String = "", // Comma-separated list stored as string
    val incomeLevel: String = "low",
    val language: String = "English",
    val notificationsEnabled: Boolean = true,
    val financialScore: Int = 68,
    val memberLevel: String = "Silver Member",
    val createdAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
