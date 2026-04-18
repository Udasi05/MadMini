package com.example.myminiproject.data.model

data class SupportTicket(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userPhone: String = "",
    val subject: String = "",
    val description: String = "",
    val status: String = "open", // open, in_progress, resolved, closed
    val priority: String = "medium", // low, medium, high
    val category: String = "general", // general, technical, billing, feature_request
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val assignedTo: String = "",
    val responses: List<TicketResponse> = emptyList()
)

data class TicketResponse(
    val id: String = "",
    val message: String = "",
    val isFromSupport: Boolean = false,
    val authorName: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class CreateTicketRequest(
    val userId: String,
    val subject: String,
    val description: String,
    val category: String = "general"
)

data class TicketListResponse(
    val tickets: List<SupportTicket>,
    val totalCount: Int
)