package com.example.myminiproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val subject: String = "",
    val description: String = "",
    val status: String = "open",
    val priority: String = "medium",
    val category: String = "general",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
