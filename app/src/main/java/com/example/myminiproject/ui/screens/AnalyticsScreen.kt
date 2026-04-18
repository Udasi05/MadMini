package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.AnalyticsViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController) {
    val analyticsViewModel: AnalyticsViewModel = viewModel()
    
    val monthlyData by analyticsViewModel.monthlyData.collectAsState()
    val categoryBreakdown by analyticsViewModel.categoryBreakdown.collectAsState()
    val insights by analyticsViewModel.insights.collectAsState()
    val isLoading by analyticsViewModel.isLoading.collectAsState()
    
    LaunchedEffect(Unit) {
        analyticsViewModel.loadAnalytics()
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Financial Analytics", fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { analyticsViewModel.refreshData() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
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
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Monthly Trend Card
                MonthlyTrendCard(monthlyData)
                
                // Category Breakdown
                CategoryBreakdownCard(categoryBreakdown)
                
                // Smart Insights
                SmartInsightsCard(insights)
                
                // Spending Patterns
                SpendingPatternsCard()
                
                // Financial Health Score
                FinancialHealthCard()
            }
        }
    }
}

@Composable
private fun MonthlyTrendCard(monthlyData: List<MonthlyDataPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Monthly Trend",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Gray800
                )
                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = Green600,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simple chart representation
            if (monthlyData.isNotEmpty()) {
                val maxAmount = monthlyData.maxOfOrNull { maxOf(it.income, it.expense) } ?: 1.0
                
                monthlyData.takeLast(6).forEach { data ->
                    MonthlyBarItem(
                        month = data.month,
                        income = data.income,
                        expense = data.expense,
                        maxAmount = maxAmount
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                Text(
                    "No data available",
                    color = Gray500,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun MonthlyBarItem(
    month: String,
    income: Double,
    expense: Double,
    maxAmount: Double
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                month,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Gray600
            )
            Text(
                "Net: ${formatter.format(income - expense)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (income > expense) Green600 else Red600
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Income bar
            Box(
                modifier = Modifier
                    .weight((income / maxAmount).toFloat())
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Green600)
            )
            
            // Expense bar
            Box(
                modifier = Modifier
                    .weight((expense / maxAmount).toFloat())
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Red600)
            )
            
            // Remaining space
            val remaining = 1.0 - (income + expense) / maxAmount
            if (remaining > 0) {
                Box(
                    modifier = Modifier
                        .weight(remaining.toFloat())
                        .height(8.dp)
                )
            }
        }
    }
}

@Composable
private fun CategoryBreakdownCard(categoryData: List<CategoryDataPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Expense Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (categoryData.isNotEmpty()) {
                val totalExpense = categoryData.sumOf { it.amount }
                
                categoryData.forEach { category ->
                    CategoryItem(
                        category = category,
                        percentage = (category.amount / totalExpense * 100).toInt()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Text(
                    "No expense data available",
                    color = Gray500,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(category: CategoryDataPoint, percentage: Int) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                category.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Gray800
            )
            Text(
                "${formatter.format(category.amount)} ($percentage%)",
                fontSize = 12.sp,
                color = Gray600
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        LinearProgressIndicator(
            progress = percentage / 100f,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = category.color,
            trackColor = Gray200
        )
    }
}

@Composable
private fun SmartInsightsCard(insights: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Blue50),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Blue600,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "Smart Insights",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue800
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            insights.forEach { insight ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Circle,
                        contentDescription = null,
                        tint = Blue600,
                        modifier = Modifier.size(6.dp)
                    )
                    Text(
                        insight,
                        fontSize = 14.sp,
                        color = Blue800,
                        lineHeight = 20.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SpendingPatternsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Spending Patterns",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Weekly pattern
            PatternItem(
                title = "Peak Spending Day",
                value = "Tuesday",
                icon = Icons.Default.CalendarToday,
                color = Orange600
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PatternItem(
                title = "Average Daily Expense",
                value = "₹245",
                icon = Icons.Default.TrendingDown,
                color = Red600
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            PatternItem(
                title = "Savings Rate",
                value = "23%",
                icon = Icons.Default.Savings,
                color = Green600
            )
        }
    }
}

@Composable
private fun PatternItem(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                title,
                fontSize = 14.sp,
                color = Gray600
            )
        }
        
        Text(
            value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray800
        )
    }
}

@Composable
private fun FinancialHealthCard() {
    val healthScore = 78 // This would be calculated based on user's financial data
    val healthColor = when {
        healthScore >= 80 -> Green600
        healthScore >= 60 -> Orange600
        else -> Red600
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Financial Health Score",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = healthScore / 100f,
                        modifier = Modifier.size(80.dp),
                        color = healthColor,
                        strokeWidth = 8.dp,
                        trackColor = Gray200
                    )
                    Text(
                        "$healthScore",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = healthColor
                    )
                }
                
                Column {
                    Text(
                        when {
                            healthScore >= 80 -> "Excellent"
                            healthScore >= 60 -> "Good"
                            else -> "Needs Improvement"
                        },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = healthColor
                    )
                    Text(
                        "Based on your spending habits, savings rate, and financial goals",
                        fontSize = 12.sp,
                        color = Gray600,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

// Data classes for analytics
data class MonthlyDataPoint(
    val month: String,
    val income: Double,
    val expense: Double
)

data class CategoryDataPoint(
    val name: String,
    val amount: Double,
    val color: Color
)