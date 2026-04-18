package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.data.model.Transaction
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.TransactionState
import com.example.myminiproject.ui.viewmodels.TransactionViewModel
import com.example.myminiproject.utils.NotificationHelper
import com.example.myminiproject.utils.ToastHelper
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TrackMoneyScreen(navController: NavController, viewModel: TransactionViewModel = viewModel()) {
        val context = LocalContext.current
        val notificationHelper = remember { NotificationHelper(context) }
        var activeTab by remember { mutableStateOf("all") }
        var showAdd by remember { mutableStateOf(false) }
        var txType by remember { mutableStateOf("income") }
        var formTitle by remember { mutableStateOf("") }
        var formAmount by remember { mutableStateOf("") }
        var formCategory by remember { mutableStateOf("") }

        val transactions by viewModel.transactions.collectAsState()
        val stats by viewModel.stats.collectAsState()
        val transactionState by viewModel.transactionState.collectAsState()

        // Load transactions on first composition
        LaunchedEffect(Unit) { viewModel.loadTransactions() }

        // Handle success state
        LaunchedEffect(transactionState) {
                if (transactionState is TransactionState.Success) {
                        // Save values before clearing for notification and toast
                        val savedAmount = formAmount
                        val savedTitle = formTitle
                        val savedCategory = formCategory
                        val savedType = txType

                        // Show notification based on transaction type
                        if (savedType == "income") {
                                notificationHelper.showIncomeAdded(
                                        savedAmount.toDoubleOrNull() ?: 0.0,
                                        savedTitle
                                )
                        } else {
                                notificationHelper.showExpenseAdded(
                                        savedAmount.toDoubleOrNull() ?: 0.0,
                                        savedCategory
                                )
                        }

                        // Show toast with correct amount
                        ToastHelper.showTransactionAdded(context, savedAmount)

                        // Clear form fields and close popup
                        formTitle = ""
                        formAmount = ""
                        formCategory = ""
                        showAdd = false

                        // Reset ViewModel state
                        viewModel.resetState()
                } else if (transactionState is TransactionState.Error) {
                        val error = (transactionState as TransactionState.Error).message
                        ToastHelper.showTransactionError(context, error)
                }
        }

        val filtered =
                when (activeTab) {
                        "income" -> transactions.filter { it.type == "income" }
                        "expense" -> transactions.filter { it.type == "expense" }
                        else -> transactions
                }
        val totalIncome = stats.totalIncome
        val totalExpense = stats.totalExpense
        val fmt = NumberFormat.getNumberInstance(Locale("en", "IN"))

        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(Gray50)
                                .verticalScroll(rememberScrollState())
        ) {
                // Header
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .background(
                                                brush =
                                                        Brush.linearGradient(
                                                                colors = listOf(Blue900, Blue600),
                                                                start = Offset(0f, 0f),
                                                                end =
                                                                        Offset(
                                                                                Float.POSITIVE_INFINITY,
                                                                                Float.POSITIVE_INFINITY
                                                                        )
                                                        )
                                        )
                                        .padding(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 32.dp,
                                                bottom = 16.dp
                                        )
                ) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        stringResource(R.string.track_money),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                )
                                IconButton(
                                        onClick = { showAdd = true },
                                        modifier =
                                                Modifier.size(36.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.White.copy(alpha = 0.2f))
                                ) {
                                        Icon(
                                                Icons.Default.Add,
                                                contentDescription =
                                                        stringResource(R.string.add_transaction),
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stats
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Card(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor =
                                                                Color.White.copy(alpha = 0.15f)
                                                )
                                ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                                Row(
                                                        verticalAlignment =
                                                                Alignment.CenterVertically,
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(6.dp)
                                                ) {
                                                        Icon(
                                                                Icons.Default.TrendingUp,
                                                                contentDescription = null,
                                                                tint = Green400,
                                                                modifier = Modifier.size(14.dp)
                                                        )
                                                        Text(
                                                                stringResource(
                                                                        R.string.total_income
                                                                ),
                                                                fontSize = 11.sp,
                                                                color = Blue200
                                                        )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                        "₹${fmt.format(totalIncome.toLong())}",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                        }
                                }
                                Card(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors =
                                                CardDefaults.cardColors(
                                                        containerColor =
                                                                Color.White.copy(alpha = 0.15f)
                                                )
                                ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                                Row(
                                                        verticalAlignment =
                                                                Alignment.CenterVertically,
                                                        horizontalArrangement =
                                                                Arrangement.spacedBy(6.dp)
                                                ) {
                                                        Icon(
                                                                Icons.Default.TrendingDown,
                                                                contentDescription = null,
                                                                tint = Red400,
                                                                modifier = Modifier.size(14.dp)
                                                        )
                                                        Text(
                                                                stringResource(
                                                                        R.string.total_expense
                                                                ),
                                                                fontSize = 11.sp,
                                                                color = Blue200
                                                        )
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                        "₹${fmt.format(totalExpense.toLong())}",
                                                        fontSize = 18.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White
                                                )
                                        }
                                }
                        }
                }

                // Pie Chart
                Card(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        stringResource(R.string.income_breakdown),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Slate800
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                // Income Breakdown Chart
                                val incomeTransactions = transactions.filter { it.type == "income" }
                                val incomeByCategory =
                                        incomeTransactions.groupBy { it.category }.mapValues {
                                                (_, txs) ->
                                                txs.sumOf { it.amount }.toFloat()
                                        }

                                if (incomeByCategory.isNotEmpty()) {
                                        val pieData = incomeByCategory.toList()
                                        val colors =
                                                listOf(
                                                        Blue600,
                                                        Green500,
                                                        Amber500,
                                                        Purple500,
                                                        Red400
                                                )
                                        val total = pieData.sumOf { it.second.toDouble() }.toFloat()

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                // Donut chart
                                                Canvas(modifier = Modifier.size(100.dp)) {
                                                        var startAngle = -90f
                                                        pieData.forEachIndexed { idx, (_, value) ->
                                                                val sweep = (value / total) * 360f
                                                                drawArc(
                                                                        color =
                                                                                colors[
                                                                                        idx %
                                                                                                colors.size],
                                                                        startAngle = startAngle,
                                                                        sweepAngle = sweep,
                                                                        useCenter = false,
                                                                        style = Stroke(width = 22f),
                                                                        topLeft = Offset(11f, 11f),
                                                                        size =
                                                                                Size(
                                                                                        size.width -
                                                                                                22f,
                                                                                        size.height -
                                                                                                22f
                                                                                )
                                                                )
                                                                startAngle += sweep
                                                        }
                                                }

                                                Spacer(modifier = Modifier.width(16.dp))

                                                Column(
                                                        verticalArrangement =
                                                                Arrangement.spacedBy(8.dp)
                                                ) {
                                                        pieData.take(4).forEachIndexed {
                                                                idx,
                                                                (name, value) ->
                                                                Row(
                                                                        verticalAlignment =
                                                                                Alignment
                                                                                        .CenterVertically,
                                                                        horizontalArrangement =
                                                                                Arrangement
                                                                                        .spacedBy(
                                                                                                8.dp
                                                                                        )
                                                                ) {
                                                                        Box(
                                                                                modifier =
                                                                                        Modifier.size(
                                                                                                        12.dp
                                                                                                )
                                                                                                .clip(
                                                                                                        CircleShape
                                                                                                )
                                                                                                .background(
                                                                                                        colors[
                                                                                                                idx %
                                                                                                                        colors.size]
                                                                                                )
                                                                        )
                                                                        Text(
                                                                                name,
                                                                                fontSize = 12.sp,
                                                                                color = Gray700,
                                                                                modifier =
                                                                                        Modifier.weight(
                                                                                                1f
                                                                                        )
                                                                        )
                                                                        Text(
                                                                                "₹${fmt.format(value.toLong())}",
                                                                                fontSize = 12.sp,
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .SemiBold,
                                                                                color = Gray900
                                                                        )
                                                                }
                                                        }
                                                }
                                        }
                                } else {
                                        // Empty state
                                        Column(
                                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                                Icon(
                                                        Icons.Default.PieChart,
                                                        contentDescription = null,
                                                        tint = Gray300,
                                                        modifier = Modifier.size(40.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                        "No income data yet",
                                                        fontSize = 13.sp,
                                                        color = Gray500
                                                )
                                                Text(
                                                        "Add some income transactions",
                                                        fontSize = 11.sp,
                                                        color = Gray400
                                                )
                                        }
                                }
                        }
                }

                // Filter Tabs
                Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                                listOf(
                                                "all" to stringResource(R.string.all),
                                                "income" to stringResource(R.string.income),
                                                "expense" to stringResource(R.string.expenses)
                                        )
                                        .forEach { (key, label) ->
                                                Button(
                                                        onClick = { activeTab = key },
                                                        modifier =
                                                                Modifier.weight(1f).height(40.dp),
                                                        shape = RoundedCornerShape(8.dp),
                                                        colors =
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                if (activeTab == key
                                                                                )
                                                                                        Blue600
                                                                                else
                                                                                        Color.Transparent,
                                                                        contentColor =
                                                                                if (activeTab == key
                                                                                )
                                                                                        Color.White
                                                                                else Gray500
                                                                ),
                                                        contentPadding = PaddingValues(0.dp)
                                                ) {
                                                        Text(
                                                                label,
                                                                fontSize = 13.sp,
                                                                fontWeight =
                                                                        if (activeTab == key)
                                                                                FontWeight.SemiBold
                                                                        else FontWeight.Normal
                                                        )
                                                }
                                        }
                        }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Transactions List
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
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .padding(32.dp),
                                                        horizontalAlignment =
                                                                Alignment.CenterHorizontally
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
                                                        modifier =
                                                                Modifier.fillMaxWidth()
                                                                        .padding(32.dp),
                                                        horizontalAlignment =
                                                                Alignment.CenterHorizontally
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
                                                                (transactionState as
                                                                                TransactionState.Error)
                                                                        .message,
                                                                fontSize = 12.sp,
                                                                color = Gray500
                                                        )
                                                        Spacer(modifier = Modifier.height(8.dp))
                                                        Button(
                                                                onClick = {
                                                                        viewModel.loadTransactions()
                                                                },
                                                                colors =
                                                                        ButtonDefaults.buttonColors(
                                                                                containerColor =
                                                                                        Blue600
                                                                        )
                                                        ) { Text("Retry", color = Color.White) }
                                                }
                                        }
                                        else -> {
                                                if (filtered.isEmpty()) {
                                                        // Empty state
                                                        Column(
                                                                modifier =
                                                                        Modifier.fillMaxWidth()
                                                                                .padding(32.dp),
                                                                horizontalAlignment =
                                                                        Alignment.CenterHorizontally
                                                        ) {
                                                                Icon(
                                                                        Icons.Default.Receipt,
                                                                        contentDescription = null,
                                                                        tint = Gray300,
                                                                        modifier =
                                                                                Modifier.size(48.dp)
                                                                )
                                                                Spacer(
                                                                        modifier =
                                                                                Modifier.height(
                                                                                        12.dp
                                                                                )
                                                                )
                                                                Text(
                                                                        stringResource(
                                                                                R.string
                                                                                        .no_transactions_yet
                                                                        ),
                                                                        fontSize = 14.sp,
                                                                        fontWeight =
                                                                                FontWeight.Medium,
                                                                        color = Gray500
                                                                )
                                                                Text(
                                                                        "Add your first transaction",
                                                                        fontSize = 12.sp,
                                                                        color = Gray400
                                                                )
                                                        }
                                                } else {
                                                        filtered.forEachIndexed { index, tx ->
                                                                Row(
                                                                        modifier =
                                                                                Modifier.fillMaxWidth()
                                                                                        .padding(
                                                                                                horizontal =
                                                                                                        16.dp,
                                                                                                vertical =
                                                                                                        10.dp
                                                                                        ),
                                                                        verticalAlignment =
                                                                                Alignment
                                                                                        .CenterVertically,
                                                                        horizontalArrangement =
                                                                                Arrangement
                                                                                        .spacedBy(
                                                                                                12.dp
                                                                                        )
                                                                ) {
                                                                        Box(
                                                                                modifier =
                                                                                        Modifier.size(
                                                                                                        44.dp
                                                                                                )
                                                                                                .clip(
                                                                                                        RoundedCornerShape(
                                                                                                                12.dp
                                                                                                        )
                                                                                                )
                                                                                                .background(
                                                                                                        Gray50
                                                                                                ),
                                                                                contentAlignment =
                                                                                        Alignment
                                                                                                .Center
                                                                        ) {
                                                                                Text(
                                                                                        tx.icon,
                                                                                        fontSize =
                                                                                                20.sp
                                                                                )
                                                                        }
                                                                        Column(
                                                                                modifier =
                                                                                        Modifier.weight(
                                                                                                1f
                                                                                        )
                                                                        ) {
                                                                                Text(
                                                                                        tx.title,
                                                                                        fontSize =
                                                                                                14.sp,
                                                                                        fontWeight =
                                                                                                FontWeight
                                                                                                        .Medium,
                                                                                        color =
                                                                                                Gray800
                                                                                )
                                                                                Spacer(
                                                                                        modifier =
                                                                                                Modifier.height(
                                                                                                        2.dp
                                                                                                )
                                                                                )
                                                                                Row(
                                                                                        horizontalArrangement =
                                                                                                Arrangement
                                                                                                        .spacedBy(
                                                                                                                8.dp
                                                                                                        ),
                                                                                        verticalAlignment =
                                                                                                Alignment
                                                                                                        .CenterVertically
                                                                                ) {
                                                                                        Text(
                                                                                                tx.category,
                                                                                                fontSize =
                                                                                                        10.sp,
                                                                                                fontWeight =
                                                                                                        FontWeight
                                                                                                                .Medium,
                                                                                                color =
                                                                                                        if (tx.type ==
                                                                                                                        "income"
                                                                                                        )
                                                                                                                Green600
                                                                                                        else
                                                                                                                Red500,
                                                                                                modifier =
                                                                                                        Modifier.clip(
                                                                                                                        RoundedCornerShape(
                                                                                                                                4.dp
                                                                                                                        )
                                                                                                                )
                                                                                                                .background(
                                                                                                                        if (tx.type ==
                                                                                                                                        "income"
                                                                                                                        )
                                                                                                                                Green100
                                                                                                                        else
                                                                                                                                Red100
                                                                                                                )
                                                                                                                .padding(
                                                                                                                        horizontal =
                                                                                                                                6.dp,
                                                                                                                        vertical =
                                                                                                                                2.dp
                                                                                                                )
                                                                                        )
                                                                                        Text(
                                                                                                tx.date,
                                                                                                fontSize =
                                                                                                        11.sp,
                                                                                                color =
                                                                                                        Gray400
                                                                                        )
                                                                                }
                                                                        }
                                                                        Text(
                                                                                "${if (tx.type == "income") "+" else "-"}₹${fmt.format(tx.amount.toLong())}",
                                                                                fontSize = 15.sp,
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .SemiBold,
                                                                                color =
                                                                                        if (tx.type ==
                                                                                                        "income"
                                                                                        )
                                                                                                Green500
                                                                                        else Red400
                                                                        )
                                                                }
                                                                if (index < filtered.size - 1) {
                                                                        HorizontalDivider(
                                                                                modifier =
                                                                                        Modifier.padding(
                                                                                                horizontal =
                                                                                                        16.dp
                                                                                        ),
                                                                                color = Gray100
                                                                        )
                                                                }
                                                        }
                                                }
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
                        isLoading = transactionState is TransactionState.Loading,
                        onAdd = {
                                if (formTitle.isNotBlank() && formAmount.isNotBlank()) {
                                        val cat =
                                                formCategory.ifBlank {
                                                        if (txType == "income")
                                                                Transaction.incomeCategories[0]
                                                        else Transaction.expenseCategories[0]
                                                }
                                        val icon =
                                                Transaction.categoryIcons[cat]
                                                        ?: if (txType == "income") "💰" else "💸"

                                        viewModel.addTransaction(
                                                title = formTitle,
                                                amount = formAmount,
                                                type = txType,
                                                category = cat,
                                                icon = icon
                                        )
                                } else {
                                        if (formTitle.isBlank()) {
                                                ToastHelper.showFieldEmptyError(context, "title")
                                        } else {
                                                ToastHelper.showFieldEmptyError(context, "amount")
                                        }
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
        isLoading: Boolean = false,
        onAdd: () -> Unit,
        onDismiss: () -> Unit
) {
        val cats =
                if (txType == "income") Transaction.incomeCategories
                else Transaction.expenseCategories
        var expanded by remember { mutableStateOf(false) }

        ModalBottomSheet(
                onDismissRequest = onDismiss,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                containerColor = Color.White
        ) {
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .padding(bottom = 32.dp)
                ) {
                        Text(
                                stringResource(R.string.add_transaction),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // Type toggle
                        Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Gray100)
                        ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(4.dp)) {
                                        listOf(
                                                        "income" to stringResource(R.string.income),
                                                        "expense" to
                                                                stringResource(R.string.expenses)
                                                )
                                                .forEach { (key, label) ->
                                                        Button(
                                                                onClick = { onTypeChange(key) },
                                                                modifier =
                                                                        Modifier.weight(1f)
                                                                                .height(44.dp),
                                                                shape = RoundedCornerShape(8.dp),
                                                                colors =
                                                                        ButtonDefaults.buttonColors(
                                                                                containerColor =
                                                                                        if (txType ==
                                                                                                        key
                                                                                        )
                                                                                                (if (key ==
                                                                                                                "income"
                                                                                                )
                                                                                                        Green500
                                                                                                else
                                                                                                        Red400)
                                                                                        else
                                                                                                Color.Transparent,
                                                                                contentColor =
                                                                                        if (txType ==
                                                                                                        key
                                                                                        )
                                                                                                Color.White
                                                                                        else Gray500
                                                                        ),
                                                                contentPadding = PaddingValues(0.dp)
                                                        ) {
                                                                Text(
                                                                        label,
                                                                        fontSize = 14.sp,
                                                                        fontWeight =
                                                                                if (txType == key)
                                                                                        FontWeight
                                                                                                .SemiBold
                                                                                else
                                                                                        FontWeight
                                                                                                .Normal
                                                                )
                                                        }
                                                }
                                }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        OutlinedTextField(
                                value = title,
                                onValueChange = onTitleChange,
                                label = { Text(stringResource(R.string.description)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors =
                                        OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Blue600,
                                                unfocusedBorderColor = Gray200,
                                                focusedContainerColor = Gray50,
                                                unfocusedContainerColor = Gray50
                                        )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                                value = amount,
                                onValueChange = {
                                        onAmountChange(it.filter { c -> c.isDigit() || c == '.' })
                                },
                                label = { Text(stringResource(R.string.amount)) },
                                prefix = { Text("₹ ", fontWeight = FontWeight.Medium) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Number),
                                colors =
                                        OutlinedTextFieldDefaults.colors(
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
                                        value =
                                                category.ifBlank {
                                                        stringResource(R.string.select_category)
                                                },
                                        onValueChange = {},
                                        readOnly = true,
                                        trailingIcon = {
                                                ExposedDropdownMenuDefaults.TrailingIcon(
                                                        expanded = expanded
                                                )
                                        },
                                        modifier = Modifier.fillMaxWidth().menuAnchor(),
                                        shape = RoundedCornerShape(12.dp),
                                        colors =
                                                OutlinedTextFieldDefaults.colors(
                                                        focusedBorderColor = Blue600,
                                                        unfocusedBorderColor = Gray200,
                                                        focusedContainerColor = Gray50,
                                                        unfocusedContainerColor = Gray50
                                                )
                                )
                                ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                ) {
                                        cats.forEach { cat ->
                                                DropdownMenuItem(
                                                        text = { Text(cat) },
                                                        onClick = {
                                                                onCategoryChange(cat)
                                                                expanded = false
                                                        }
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
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = Gray100
                                                ),
                                        enabled = !isLoading
                                ) {
                                        Text(
                                                stringResource(R.string.cancel),
                                                color = Gray500,
                                                fontWeight = FontWeight.Medium
                                        )
                                }
                                Button(
                                        onClick = onAdd,
                                        modifier = Modifier.weight(1f).height(48.dp),
                                        shape = RoundedCornerShape(16.dp),
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = Blue600
                                                ),
                                        enabled = !isLoading
                                ) {
                                        if (isLoading) {
                                                CircularProgressIndicator(
                                                        modifier = Modifier.size(20.dp),
                                                        color = Color.White,
                                                        strokeWidth = 2.dp
                                                )
                                        } else {
                                                Text(
                                                        stringResource(R.string.save),
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        }
                                }
                        }
                }
        }
}
