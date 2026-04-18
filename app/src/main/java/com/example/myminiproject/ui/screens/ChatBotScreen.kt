package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.ChatBotViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatBotScreen(
    navController: NavController,
    viewModel: ChatBotViewModel = viewModel()
) {
    val messages by viewModel.messages.collectAsState()
    val isTyping by viewModel.isTyping.collectAsState()

    var inputValue by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val quickActions = listOf("Check my balance", "How to save more?", "Analyze my spending", "Loan options")

    // Auto-scroll when messages change
    LaunchedEffect(messages.size, isTyping) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(listOf(Blue900, Blue600))
                )
                .padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(24.dp))
            }
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Blue600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Box(
                    modifier = Modifier.size(12.dp).clip(CircleShape).background(Green500)
                        .offset(x = 1.dp, y = 1.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Assistant", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Green400))
                    Text("Always online", fontSize = 12.sp, color = Blue200)
                }
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White.copy(alpha = 0.8f), modifier = Modifier.size(20.dp))
            }
        }

        // Chat Area
        Box(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                messages.forEach { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.sender == "user") Arrangement.End else Arrangement.Start
                    ) {
                        Row(
                            horizontalArrangement = if (msg.sender == "user") Arrangement.End else Arrangement.Start,
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            if (msg.sender == "bot") {
                                Box(
                                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Blue50),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = Blue600, modifier = Modifier.size(14.dp))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Column(horizontalAlignment = if (msg.sender == "user") Alignment.End else Alignment.Start) {
                                Card(
                                    shape = RoundedCornerShape(
                                        topStart = if (msg.sender == "bot") 4.dp else 16.dp,
                                        topEnd = if (msg.sender == "user") 4.dp else 16.dp,
                                        bottomStart = 16.dp,
                                        bottomEnd = 16.dp
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (msg.sender == "user") Blue600 else Gray100
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                                ) {
                                    Text(
                                        msg.text,
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        color = if (msg.sender == "user") Color.White else Gray800,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    timeFormat.format(msg.timestamp),
                                    fontSize = 10.sp,
                                    color = Gray400
                                )
                            }

                            if (msg.sender == "user") {
                                Spacer(modifier = Modifier.width(8.dp))
                                Box(
                                    modifier = Modifier.size(32.dp).clip(CircleShape).background(Gray100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = Gray600, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }

                // Typing indicator
                if (isTyping) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            shape = RoundedCornerShape(topStart = 4.dp, topEnd = 16.dp, bottomStart = 16.dp, bottomEnd = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Gray100),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(3) {
                                    Box(
                                        modifier = Modifier.size(6.dp).clip(CircleShape).background(Gray400)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))
            }

            // Floating Quick Actions
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickActions.forEach { action ->
                    AssistChip(
                        onClick = { inputValue = action },
                        label = { Text(action, fontSize = 12.sp) },
                        shape = RoundedCornerShape(20.dp),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color.White,
                            labelColor = Gray600
                        ),
                        border = AssistChipDefaults.assistChipBorder(enabled = true, borderColor = Gray200),
                        elevation = AssistChipDefaults.assistChipElevation(elevation = 2.dp)
                    )
                }
            }
        }

        // Input Area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            HorizontalDivider(color = Gray100)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Gray50)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Gray400, modifier = Modifier.size(20.dp))
                }

                OutlinedTextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    placeholder = { Text("Type your message...", fontSize = 14.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    trailingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            IconButton(onClick = {}, modifier = Modifier.size(36.dp)) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice", tint = Gray400, modifier = Modifier.size(20.dp))
                            }
                            IconButton(
                                onClick = {
                                    viewModel.sendMessage(inputValue)
                                    inputValue = ""
                                },
                                enabled = inputValue.isNotBlank() && !isTyping,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (inputValue.isNotBlank() && !isTyping) Blue600 else Gray200)
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = if (inputValue.isNotBlank() && !isTyping) Color.White else Gray400,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gray200,
                        unfocusedBorderColor = Gray200,
                        focusedContainerColor = Gray50.copy(alpha = 0.5f),
                        unfocusedContainerColor = Gray50.copy(alpha = 0.5f)
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Assistant can make mistakes. Check important info.",
                fontSize = 9.sp,
                color = Gray400,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
