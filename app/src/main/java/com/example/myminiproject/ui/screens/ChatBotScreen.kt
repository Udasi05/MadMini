package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
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
import androidx.navigation.NavController
import com.example.myminiproject.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String, // "user" or "bot"
    val timestamp: Date = Date()
)

@Composable
fun ChatBotScreen(navController: NavController) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = "1",
                    text = "Hello! I'm your DhanSathi assistant. How can I help you with your finances today? 👋",
                    sender = "bot"
                )
            )
        )
    }
    var inputValue by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    val quickActions = listOf("Check my balance", "How to save more?", "Analyze my spending", "Loan options")

    fun getBotResponse(input: String): String {
        val lower = input.lowercase()
        return when {
            lower.contains("saving") || lower.contains("save") ->
                "Based on your recent transactions, you could save approximately ₹1,200 more per month by reducing recurring entertainment subscriptions. Would you like to see a breakdown?"
            lower.contains("balance") ->
                "Your current total balance is ₹24,850. You have ₹18,400 in income and ₹7,550 in expenses this month."
            lower.contains("spending") || lower.contains("expense") || lower.contains("analyze") ->
                "Your highest spending category this week is 'Groceries' at ₹3,200. This is 15% lower than your average! Great job! 📉"
            lower.contains("loan") ->
                "Government loans like Kisan Credit Card and Mudra Loan offer rates of 4-12%. Much cheaper than private lenders. Visit your nearest bank to apply."
            else ->
                "That's an interesting question about your finances. I'm currently analyzing your data to give you the best advice. Can you tell me more about your specific goal?"
        }
    }

    fun sendMessage() {
        if (inputValue.isBlank()) return
        val userMsg = inputValue.trim()
        messages = messages + ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = userMsg,
            sender = "user"
        )
        inputValue = ""
        isTyping = true

        scope.launch {
            delay(1500)
            messages = messages + ChatMessage(
                id = (System.currentTimeMillis() + 1).toString(),
                text = getBotResponse(userMsg),
                sender = "bot"
            )
            isTyping = false
        }
    }

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
                                onClick = { sendMessage() },
                                enabled = inputValue.isNotBlank(),
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (inputValue.isNotBlank()) Blue600 else Gray200)
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = if (inputValue.isNotBlank()) Color.White else Gray400,
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
