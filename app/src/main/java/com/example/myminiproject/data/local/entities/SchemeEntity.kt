package com.example.myminiproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schemes")
data class SchemeEntity(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val fullName: String = "",
    val category: String = "",
    val emoji: String = "",
    val benefit: String = "",
    val benefitColorHex: Long = 0xFF10B981,
    val description: String = "",
    val eligibility: String = "",
    val howToApply: String = "",
    val documents: String = "", // Comma-separated list stored as string
    val tag: String = "",
    val tagColorHex: Long = 0xFF10B981,
    val lastUpdated: Long = System.currentTimeMillis()
)
