package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.data.model.Transaction
import com.example.myminiproject.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TrackMoneyScreen(navController: NavController) {
    var activeTab by remember { mutableStateOf("all") }
    var showAdd by remember { mutableStateOf(false) }
    var txType by remember { mutableStateOf("income") }
    var formTitle by remember { mutableStateOf("") }
    var formAmount by remember { mutableStateOf("") }
    var formCategory by remember { mutableStateOf("") }
    var transactions by remember { mutableStateOf(Transaction.getSampleTransactions()) }

    val filtered = when (activeTab) {
        "income" -> transactions.filter { it.type == "income" }
        "expense" -> transactions.filter { it.type == "expense" }
        else -> transactions
    }
    val totalIncome = transactions.filter { it.type == "income" }.sumOf { it.amount }
    val totalExpense = transactions.filter { it.type == "expense" }.sumOf { it.amount }
    val fmt = NumberFormat.getNumberInstance(Locale("en", "IN"))

    Column(
        modifier = Modifier.fillMaxSize().background(Gray50)
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
                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Track Money", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                IconButton(
                    onClick = { showAdd = true },
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Stats
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Green400, modifier = Modifier.size(14.dp))
                            Text("Total Income", fontSize = 11.sp, color = Blue200)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("₹${fmt.format(totalIncome.toLong())}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.TrendingDown, contentDescription = null, tint = Red400, modifier = Modifier.size(14.dp))
                            Text("Total Expense", fontSize = 11.sp, color = Blue200)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("₹${fmt.format(totalExpense.toLong())}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }

        // Pie Chart
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Income Breakdown", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Slate800)
                Spacer(modifier = Modifier.height(12.dp))

                val pieData = listOf(
                    "Farm Income" to 4550f,
                    "Daily Wage" to 1400f,
                    "Business" to 2000f,
                    "Other" to 450f
                )
                val colors = listOf(Blue600, Green500, Amber500, Purple500)
                val total = pieData.sumOf { it.second.toDouble() }.toFloat()

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Donut chart
                    Canvas(modifier = Modifier.size(120.dp)) {
                        var startAngle = -90f
                        pieData.forEachIndexed { idx, (_, value) ->
                            val sweep = (value / total) * 360f
                            drawArc(
                                color = colors[idx],
                                startAngle = startAngle,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = 28f),
                                topLeft = Offset(14f, 14f),
                                size = Size(size.width - 28f, size.height - 28f)
                            )
                            startAngle += sweep
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        pieData.forEachIndexed { idx, (name, value) ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(colors[idx]))
                                Text(name, fontSize = 12.sp, color = Gray700, modifier = Modifier.weight(1f))
                                Text("₹${fmt.format(value.toLong())}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gray900)
                            }
                        }
                    }
                }
            }
        }

        // Filter Tabs
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                listOf("all" to "All", "income" to "Income", "expense" to "Expense").forEach { (key, label) ->
                    Button(
                        onClick = { activeTab = key },
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (activeTab == key) Blue600 else Color.Transparent,
                            contentColor = if (activeTab == key) Color.White else Gray500
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(label, fontSize = 13.sp, fontWeight = if (activeTab == key) FontWeight.SemiBold else FontWeight.Normal)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Transactions List
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                filtered.forEachIndexed { index, tx ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
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
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    tx.category,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (tx.type == "income") Green600 else Red500,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (tx.type == "income") Green100 else Red100)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                Text(tx.date, fontSize = 11.sp, color = Gray400)
                            }
                        }
                        Text(
                            "${if (tx.type == "income") "+" else "-"}₹${fmt.format(tx.amount.toLong())}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (tx.type == "income") Green500 else Red400
                        )
                    }
                    if (index < filtered.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray100)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Add Transaction Bottom Sheet
    if (showAdd) {
        AddTransactionSheet(
            txType = txType,
            onTypeChange = { txType = it },
            title = formTitle,
            onTitleChange = { formTitle = it },
            amount = formAmount,
            onAmountChange = { formAmount = it },
            category = formCategory,
            onCategoryChange = { formCategory = it },
            onAdd = {
                if (formTitle.isNotBlank() && formAmount.isNotBlank()) {
                    val cat = formCategory.ifBlank {
                        if (txType == "income") Transaction.incomeCategories[0] else Transaction.expenseCategories[0]
                    }
                    transactions = listOf(
                        Transaction(
                            id = System.currentTimeMillis().toString(),
                            title = formTitle,
                            amount = formAmount.toDoubleOrNull() ?: 0.0,
                            type = txType,
                            category = cat,
                            icon = Transaction.categoryIcons[cat] ?: if (txType == "income") "💰" else "💸",
                            date = "Today"
                        )
                    ) + transactions
                    formTitle = ""
                    formAmount = ""
                    formCategory = ""
                    showAdd = false
                }
            },
            onDismiss = { showAdd = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionSheet(
    txType: String,
    onTypeChange: (String) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    onAdd: () -> Unit,
    onDismiss: () -> Unit
) {
    val cats = if (txType == "income") Transaction.incomeCategories else Transaction.expenseCategories
    var expanded by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Add Transaction", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(20.dp))

            // Type toggle
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Gray100)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                    listOf("income" to "📈 Income", "expense" to "📉 Expense").forEach { (key, label) ->
                        Button(
                            onClick = { onTypeChange(key) },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (txType == key) (if (key == "income") Green500 else Red400) else Color.Transparent,
                                contentColor = if (txType == key) Color.White else Gray500
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(label, fontSize = 14.sp, fontWeight = if (txType == key) FontWeight.SemiBold else FontWeight.Normal)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray200,
                    focusedContainerColor = Gray50,
                    unfocusedContainerColor = Gray50
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { onAmountChange(it.filter { c -> c.isDigit() || c == '.' }) },
                label = { Text("Amount") },
                prefix = { Text("₹ ", fontWeight = FontWeight.Medium) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue600,
                    unfocusedBorderColor = Gray200,
                    focusedContainerColor = Gray50,
                    unfocusedContainerColor = Gray50
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Category dropdown
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = category.ifBlank { "Select Category" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue600,
                        unfocusedBorderColor = Gray200,
                        focusedContainerColor = Gray50,
                        unfocusedContainerColor = Gray50
                    )
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    cats.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = { onCategoryChange(cat); expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gray100)
                ) {
                    Text("Cancel", color = Gray500, fontWeight = FontWeight.Medium)
                }
                Button(
                    onClick = onAdd,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue600)
                ) {
                    Text("Add", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
