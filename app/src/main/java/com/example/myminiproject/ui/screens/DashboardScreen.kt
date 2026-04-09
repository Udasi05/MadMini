package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.data.model.Transaction
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*

@Composable
fun DashboardScreen(navController: NavController) {
    val transactions = remember { Transaction.getSampleTransactions().take(5) }
    var showVoice by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Blue900, Blue600),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 24.dp)
        ) {
            // Greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Good Morning 👋", fontSize = 13.sp, color = Blue200)
                    Text("Ramesh Kumar", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Balance", fontSize = 13.sp, color = Blue200)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("₹24,850", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Income
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(Green400.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Green400, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("Income", fontSize = 11.sp, color = Blue200)
                                Text("₹18,400", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                        }

                        Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color.White.copy(alpha = 0.2f)))

                        // Expenses
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(Red400.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.TrendingDown, contentDescription = null, tint = Red400, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("Expenses", fontSize = 11.sp, color = Blue200)
                                Text("₹7,550", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickAction(icon = Icons.Default.Add, label = "Add\nIncome", color = Green500, bgColor = Green100) {
                    navController.navigate(Screen.TrackMoney.route)
                }
                QuickAction(icon = Icons.Default.TrendingDown, label = "Add\nExpense", color = Red400, bgColor = Red100) {
                    navController.navigate(Screen.TrackMoney.route)
                }
                QuickAction(icon = Icons.Default.Mic, label = "Voice\nInput", color = Purple500, bgColor = Purple100) {
                    showVoice = true
                }
                QuickAction(icon = Icons.Default.Chat, label = "Assistant", color = Blue600, bgColor = Blue100) {
                    navController.navigate(Screen.ChatBot.route)
                }
            }
        }

        // Weekly Chart placeholder
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("This Week", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    Text(
                        "↑ 12% vs last week",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Green600,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Green100)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Simple bar chart
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val data = listOf(
                        "Mon" to 0.44f, "Tue" to 0.33f, "Wed" to 0.67f,
                        "Thu" to 0.89f, "Fri" to 0.56f, "Sat" to 1.0f, "Sun" to 0.83f
                    )
                    data.forEach { (day, fraction) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .fillMaxHeight(fraction)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Blue600, Blue600.copy(alpha = 0.3f))
                                        )
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(day, fontSize = 9.sp, color = Gray400)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Smart Insight Banner
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Purple600, Color(0xFF4F46E5))
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = null, tint = Yellow300, modifier = Modifier.size(20.dp))
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Smart Insight", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text("You saved ₹2,300 more than last month! 🎉", fontSize = 12.sp, color = Purple400)
                }
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White.copy(alpha = 0.6f), modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Recent Transactions
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Transactions", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
            TextButton(onClick = { navController.navigate(Screen.TrackMoney.route) }) {
                Text("See all", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Blue600)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                transactions.forEachIndexed { index, tx ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Gray50),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(tx.icon, fontSize = 20.sp)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(tx.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray800)
                            Text(tx.date, fontSize = 11.sp, color = Gray400)
                        }
                        Text(
                            text = "${if (tx.type == "income") "+" else "-"}₹${String.format("%,.0f", tx.amount)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (tx.type == "income") Green500 else Red400
                        )
                    }
                    if (index < transactions.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray100)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Voice Input Modal
    if (showVoice) {
        VoiceInputSheet(onDismiss = { showVoice = false })
    }
}

@Composable
private fun QuickAction(
    icon: ImageVector,
    label: String,
    color: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Gray600,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoiceInputSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Voice Input", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Say something like \"Aaj 500 rupaye ki sabzi becha\"",
                fontSize = 13.sp,
                color = Gray500,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.size(96.dp).clip(CircleShape).background(Blue100),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Mic, contentDescription = "Record", tint = Blue600, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Tap to start recording", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Blue600)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Supports Hindi, Tamil, Telugu, Bengali & more", fontSize = 12.sp, color = Gray400)
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Gray100)
            ) {
                Text("Cancel", color = Gray600, fontWeight = FontWeight.Medium)
            }
        }
    }
}
