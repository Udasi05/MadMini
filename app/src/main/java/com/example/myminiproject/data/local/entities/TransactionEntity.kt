package com.example.myminiproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val type: String = "income",
    val category: String = "",
    val icon: String = "₹",
    val date: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
