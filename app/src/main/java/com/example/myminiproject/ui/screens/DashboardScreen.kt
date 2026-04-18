package com.example.myminiproject.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.TransactionViewModel
import com.example.myminiproject.ui.viewmodels.TransactionState
import com.example.myminiproject.utils.NotificationHelper
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    navController: NavController,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    
    val transactions by transactionViewModel.transactions.collectAsState()
    val stats by transactionViewModel.stats.collectAsState()
    val transactionState by transactionViewModel.transactionState.collectAsState()
    
    // Check notification permission
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Show a sample notification
            notificationHelper.showTransactionReminder()
        }
    }
    
    // Load transactions on first composition
    LaunchedEffect(Unit) {
        transactionViewModel.loadTransactions()
    }
    
    val recentTransactions = transactions.take(5)
    val fmt = NumberFormat.getNumberInstance(Locale("en", "IN"))

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
                .padding(start = 16.dp, end = 16.dp, top = 32.dp, bottom = 16.dp)
        ) {
            // Greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                    val userName = user?.displayName.takeIf { !it.isNullOrBlank() } ?: "User"
                    Text(stringResource(R.string.good_morning), fontSize = 13.sp, color = Blue200)
                    Text(userName, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }
                IconButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            // Show a test notification to demonstrate the system
                            notificationHelper.showRandomNotification()
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = stringResource(R.string.notifications), tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Balance Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.total_balance), fontSize = 13.sp, color = Blue200)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("₹${fmt.format(stats.balance.toLong())}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                                Text(stringResource(R.string.income), fontSize = 11.sp, color = Blue200)
                                Text("₹${fmt.format(stats.totalIncome.toLong())}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
                                Text(stringResource(R.string.expenses), fontSize = 11.sp, color = Blue200)
                                Text("₹${fmt.format(stats.totalExpense.toLong())}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
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
                .padding(horizontal = 16.dp)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                QuickAction(icon = Icons.Default.Add, label = stringResource(R.string.add_income), color = Green500, bgColor = Green100) {
                    navController.navigate(Screen.TrackMoney.route)
                }
                QuickAction(icon = Icons.Default.TrendingDown, label = stringResource(R.string.add_expense), color = Red400, bgColor = Red100) {
                    navController.navigate(Screen.TrackMoney.route)
                }
                QuickAction(icon = Icons.Default.Mic, label = stringResource(R.string.voice_input), color = Purple500, bgColor = Purple100) {
                    navController.navigate(Screen.VoiceInput.route)
                }
                QuickAction(icon = Icons.Default.Chat, label = stringResource(R.string.assistant), color = Blue600, bgColor = Blue100) {
                    navController.navigate(Screen.ChatBot.route)
                }
            }
        }

        // Weekly Chart
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.this_week), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                    if (transactions.isNotEmpty()) {
                        val weeklyIncome = transactions.filter { it.type == "income" }.sumOf { it.amount }
                        val weeklyExpense = transactions.filter { it.type == "expense" }.sumOf { it.amount }
                        val weeklyBalance = weeklyIncome - weeklyExpense
                        val changePercent = if (weeklyBalance > 0) "+${String.format("%.0f", (weeklyBalance / weeklyIncome) * 100)}%" else "0%"
                        
                        Text(
                            "$changePercent vs last week",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (weeklyBalance > 0) Green600 else Gray500,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (weeklyBalance > 0) Green100 else Gray100)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (transactions.isNotEmpty()) {
                    // Calculate daily data for the week
                    val calendar = java.util.Calendar.getInstance()
                    val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                    val dailyAmounts = mutableListOf<Float>()
                    
                    // Get last 7 days of transaction data
                    for (i in 6 downTo 0) {
                        calendar.add(java.util.Calendar.DAY_OF_YEAR, -i)
                        val dayTransactions = transactions.filter { tx ->
                            // Simple date matching - you might want to improve this
                            tx.date.contains(java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(calendar.time))
                        }
                        val dayTotal = dayTransactions.sumOf { if (it.type == "income") it.amount else -it.amount }
                        dailyAmounts.add(maxOf(0f, dayTotal.toFloat()))
                        calendar.add(java.util.Calendar.DAY_OF_YEAR, i) // Reset
                    }
                    
                    val maxAmount = dailyAmounts.maxOrNull() ?: 1f
                    
                    // Simple bar chart
                    Row(
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        dailyAmounts.forEachIndexed { index, amount ->
                            val fraction = if (maxAmount > 0) amount / maxAmount else 0f
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .fillMaxHeight(maxOf(0.1f, fraction))
                                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(Blue600, Blue600.copy(alpha = 0.3f))
                                            )
                                        )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(dayNames[index], fontSize = 9.sp, color = Gray400)
                            }
                        }
                    }
                } else {
                    // Empty state
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = Gray300, modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No data for this week", fontSize = 13.sp, color = Gray500)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Transactions
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.recent_transactions), fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
            TextButton(onClick = { navController.navigate(Screen.TrackMoney.route) }) {
                Text(stringResource(R.string.see_all), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Blue600)
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                when (transactionState) {
                    is TransactionState.Loading -> {
                        // Loading state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Blue600,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Loading transactions...",
                                fontSize = 14.sp,
                                color = Gray500
                            )
                        }
                    }
                    is TransactionState.Error -> {
                        // Error state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Red400,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Failed to load data",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Red600
                            )
                            Text(
                                (transactionState as TransactionState.Error).message,
                                fontSize = 12.sp,
                                color = Gray500,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { transactionViewModel.loadTransactions() },
                                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
                            ) {
                                Text("Retry", color = Color.White)
                            }
                        }
                    }
                    else -> {
                        if (recentTransactions.isEmpty()) {
                            // Empty state
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = Gray300,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    stringResource(R.string.no_transactions_yet),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray500
                                )
                                Text(
                                    stringResource(R.string.start_tracking),
                                    fontSize = 12.sp,
                                    color = Gray400
                                )
                            }
                        } else {
                            recentTransactions.forEachIndexed { index, tx ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 10.dp),
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
                                        text = "${if (tx.type == "income") "+" else "-"}₹${fmt.format(tx.amount.toLong())}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (tx.type == "income") Green500 else Red400
                                    )
                                }
                                if (index < recentTransactions.size - 1) {
                                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray100)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
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
