package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.TicketViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    val ticketViewModel: TicketViewModel = viewModel()
    val currentUser = FirebaseAuth.getInstance().currentUser
    
    var showTicketDialog by remember { mutableStateOf(false) }
    var ticketSubject by remember { mutableStateOf("") }
    var ticketDescription by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("general") }
    
    val isLoading by ticketViewModel.isLoading.collectAsState()
    val submitSuccess by ticketViewModel.submitSuccess.collectAsState()
    val errorMessage by ticketViewModel.errorMessage.collectAsState()
    
    // Handle success message
    LaunchedEffect(submitSuccess) {
        if (submitSuccess) {
            ticketSubject = ""
            ticketDescription = ""
            selectedCategory = "general"
            showTicketDialog = false
            ticketViewModel.clearSubmitSuccess()
        }
    }
    
    // Handle error message
    errorMessage?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(3000)
            ticketViewModel.clearError()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text(stringResource(R.string.help_support), fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Gray800
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Submit Ticket Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Blue600),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTicketDialog = true }
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SupportAgent,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.submit_ticket),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                stringResource(R.string.ticket_description),
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // My Tickets Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.MyTickets.route) }
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Green600.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Assignment,
                                contentDescription = null,
                                tint = Green600,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "My Support Tickets",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Gray800
                            )
                            Text(
                                "View and track your submitted tickets",
                                fontSize = 13.sp,
                                color = Gray600
                            )
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Gray400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Contact Information
            Text(
                stringResource(R.string.contact_us),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ContactCard(
                icon = Icons.Default.Email,
                title = stringResource(R.string.email),
                value = "support@dhansathi.gov.in",
                color = Blue600
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            ContactCard(
                icon = Icons.Default.Phone,
                title = stringResource(R.string.phone),
                value = "1800-XXX-XXXX (Toll Free)",
                color = Green600
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // FAQ Section
            Text(
                "Frequently Asked Questions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            FAQItem(
                question = "How do I check scheme eligibility?",
                answer = "Go to the Schemes tab and your eligibility will be automatically checked based on your profile information."
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FAQItem(
                question = "How do I add transactions?",
                answer = "You can add transactions from the Dashboard or Track Money screen. You can also use voice input for quick entry."
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FAQItem(
                question = "Is my data secure?",
                answer = "Yes, all your data is encrypted and stored securely with government-backed security standards."
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            FAQItem(
                question = "How do I change language?",
                answer = "Go to Profile > Language and select your preferred language from the list."
            )
        }
    }
    
    // Ticket Submission Dialog
    if (showTicketDialog) {
        AlertDialog(
            onDismissRequest = { showTicketDialog = false },
            title = { Text(stringResource(R.string.submit_ticket)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = ticketSubject,
                        onValueChange = { ticketSubject = it },
                        label = { Text(stringResource(R.string.ticket_subject)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Category Selection
                    var expandedCategory by remember { mutableStateOf(false) }
                    val categories = listOf(
                        "general" to "General",
                        "technical" to "Technical Issue",
                        "billing" to "Billing/Payment",
                        "feature_request" to "Feature Request"
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedCategory,
                        onExpandedChange = { expandedCategory = !expandedCategory }
                    ) {
                        OutlinedTextField(
                            value = categories.find { it.first == selectedCategory }?.second ?: "General",
                            onValueChange = { },
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }
                        ) {
                            categories.forEach { (value, label) ->
                                DropdownMenuItem(
                                    text = { Text(label) },
                                    onClick = {
                                        selectedCategory = value
                                        expandedCategory = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = ticketDescription,
                        onValueChange = { ticketDescription = it },
                        label = { Text(stringResource(R.string.ticket_description)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLines = 5
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        currentUser?.uid?.let { userId ->
                            ticketViewModel.createTicket(
                                userId = userId,
                                subject = ticketSubject,
                                description = ticketDescription,
                                category = selectedCategory
                            )
                        }
                    },
                    enabled = ticketSubject.isNotBlank() && ticketDescription.isNotBlank() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.submit))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showTicketDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    
    // Success/Error Messages
    submitSuccess.let {
        if (it) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(3000)
            }
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Green600)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            stringResource(R.string.ticket_submitted),
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
    
    errorMessage?.let { error ->
        Box(
            modifier = Modifier
                .fillMaxSize()
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

@Composable
private fun ContactCard(
    icon: ImageVector,
    title: String,
    value: String,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    title,
                    fontSize = 12.sp,
                    color = Gray500
                )
                Text(
                    value,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray800
                )
            }
        }
    }
}

@Composable
private fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    question,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray800,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    answer,
                    fontSize = 13.sp,
                    color = Gray600,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
