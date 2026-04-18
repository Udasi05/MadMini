package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.data.api.dto.SupportTicket
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.TicketViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTicketsScreen(navController: NavController) {
    val ticketViewModel: TicketViewModel = viewModel()
    val currentUser = FirebaseAuth.getInstance().currentUser
    
    val tickets by ticketViewModel.tickets.collectAsState()
    val isLoading by ticketViewModel.isLoading.collectAsState()
    val errorMessage by ticketViewModel.errorMessage.collectAsState()
    
    // Load tickets when screen opens
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { userId ->
            ticketViewModel.getUserTickets(userId)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("My Support Tickets", fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Gray800
            )
        )
        
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Blue600)
            }
        } else if (tickets.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Default.SupportAgent,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Gray400
                    )
                    Text(
                        "No Support Tickets",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray600
                    )
                    Text(
                        "You haven't submitted any support tickets yet.",
                        fontSize = 14.sp,
                        color = Gray500
                    )
                }
            }
        } else {
            // Tickets list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets) { ticket ->
                    TicketCard(ticket = ticket)
                }
            }
        }
        
        // Error message
        errorMessage?.let { error ->
            LaunchedEffect(error) {
                kotlinx.coroutines.delay(3000)
                ticketViewModel.clearError()
            }
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            error,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TicketCard(ticket: SupportTicket) {
    val statusColor = when (ticket.status) {
        "open" -> Blue600
        "in_progress" -> Orange600
        "resolved" -> Green600
        "closed" -> Gray500
        else -> Gray400
    }
    
    val priorityColor = when (ticket.priority) {
        "high" -> Red600
        "medium" -> Orange600
        "low" -> Green600
        else -> Gray400
    }
    
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    val createdDate = Date(ticket.createdAt)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with status and priority
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            ticket.status.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                    
                    // Priority badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(priorityColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            "${ticket.priority.uppercase()} PRIORITY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = priorityColor
                        )
                    }
                }
                
                Text(
                    "ID: ${ticket.id.take(8)}",
                    fontSize = 10.sp,
                    color = Gray500,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Subject
            Text(
                ticket.subject,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Description (truncated)
            Text(
                ticket.description,
                fontSize = 14.sp,
                color = Gray600,
                maxLines = 2,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Footer with date and category
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Gray400
                    )
                    Text(
                        dateFormat.format(createdDate),
                        fontSize = 12.sp,
                        color = Gray500
                    )
                }
                
                Text(
                    ticket.category.replace("_", " ").uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray400
                )
            }
            
            // Responses count if any
            if (ticket.responses.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Chat,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Blue600
                    )
                    Text(
                        "${ticket.responses.size} response${if (ticket.responses.size != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = Blue600,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}