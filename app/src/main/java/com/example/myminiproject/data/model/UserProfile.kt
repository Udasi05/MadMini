package com.example.myminiproject.data.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val language: String = "English",
    val notificationsEnabled: Boolean = true,
    val financialScore: Int = 68,
    val memberLevel: String = "Silver Member",
    val createdAt: Long = System.currentTimeMillis()
)
