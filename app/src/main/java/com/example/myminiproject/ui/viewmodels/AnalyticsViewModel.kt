package com.example.myminiproject.ui.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.ui.screens.CategoryDataPoint
import com.example.myminiproject.ui.screens.MonthlyDataPoint
import com.example.myminiproject.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AnalyticsViewModel : ViewModel() {
    private val apiService = ApiClient.apiService
    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val _monthlyData = MutableStateFlow<List<MonthlyDataPoint>>(emptyList())
    val monthlyData: StateFlow<List<MonthlyDataPoint>> = _monthlyData.asStateFlow()

    private val _categoryBreakdown = MutableStateFlow<List<CategoryDataPoint>>(emptyList())
    val categoryBreakdown: StateFlow<List<CategoryDataPoint>> = _categoryBreakdown.asStateFlow()

    private val _insights = MutableStateFlow<List<String>>(emptyList())
    val insights: StateFlow<List<String>> = _insights.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val categoryColors = mapOf(
        "Food & Grocery" to Green600,
        "Transport" to Blue600,
        "Medical" to Red600,
        "Education" to Purple600,
        "Utilities" to Orange600,
        "Other" to Gray600,
        "Farm Income" to Green500,
        "Daily Wage" to Blue500,
        "Business" to Purple500,
        "Other Income" to Orange500
    )

    fun loadAnalytics() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                currentUser?.uid?.let { userId ->
                    // Load transactions and process analytics
                    val response = apiService.getTransactions(userId)
                    
                    if (response.isSuccessful) {
                        response.body()?.let { transactionResponse ->
                            processMonthlyData(transactionResponse.transactions)
                            processCategoryBreakdown(transactionResponse.transactions)
                            generateInsights(transactionResponse.transactions)
                        }
                    }
                }
            } catch (e: Exception) {
                // Handle error
                generateDemoData()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun processMonthlyData(transactions: List<com.example.myminiproject.data.api.dto.TransactionResponse>) {
        val monthFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        
        // Group transactions by month
        val monthlyMap = mutableMapOf<String, Pair<Double, Double>>() // month to (income, expense)
        
        // Initialize last 6 months
        for (i in 5 downTo 0) {
            calendar.time = Date()
            calendar.add(Calendar.MONTH, -i)
            val monthKey = monthFormat.format(calendar.time)
            monthlyMap[monthKey] = Pair(0.0, 0.0)
        }
        
        // Process transactions
        transactions.forEach { transaction ->
            val transactionDate = Date(transaction.timestamp)
            val monthKey = monthFormat.format(transactionDate)
            
            if (monthlyMap.containsKey(monthKey)) {
                val current = monthlyMap[monthKey] ?: Pair(0.0, 0.0)
                monthlyMap[monthKey] = if (transaction.type == "income") {
                    Pair(current.first + transaction.amount, current.second)
                } else {
                    Pair(current.first, current.second + transaction.amount)
                }
            }
        }
        
        // Convert to list
        val monthlyList = monthlyMap.map { (month, data) ->
            MonthlyDataPoint(month, data.first, data.second)
        }
        
        _monthlyData.value = monthlyList
    }

    private fun processCategoryBreakdown(transactions: List<com.example.myminiproject.data.api.dto.TransactionResponse>) {
        // Group expenses by category
        val categoryMap = mutableMapOf<String, Double>()
        
        transactions.filter { it.type == "expense" }.forEach { transaction ->
            val current = categoryMap[transaction.category] ?: 0.0
            categoryMap[transaction.category] = current + transaction.amount
        }
        
        // Convert to list with colors
        val categoryList = categoryMap.map { (category, amount) ->
            CategoryDataPoint(
                name = category,
                amount = amount,
                color = categoryColors[category] ?: Gray600
            )
        }.sortedByDescending { it.amount }
        
        _categoryBreakdown.value = categoryList
    }

    private fun generateInsights(transactions: List<com.example.myminiproject.data.api.dto.TransactionResponse>) {
        val insights = mutableListOf<String>()
        
        if (transactions.isEmpty()) {
            insights.add("Start tracking your transactions to get personalized insights")
            _insights.value = insights
            return
        }
        
        val totalIncome = transactions.filter { it.type == "income" }.sumOf { it.amount }
        val totalExpense = transactions.filter { it.type == "expense" }.sumOf { it.amount }
        val savingsRate = if (totalIncome > 0) ((totalIncome - totalExpense) / totalIncome * 100) else 0.0
        
        // Savings rate insight
        when {
            savingsRate > 30 -> insights.add("Excellent! You're saving ${savingsRate.toInt()}% of your income")
            savingsRate > 20 -> insights.add("Good savings rate of ${savingsRate.toInt()}%. Try to reach 30% for better financial health")
            savingsRate > 0 -> insights.add("You're saving ${savingsRate.toInt()}% of income. Consider reducing expenses to save more")
            else -> insights.add("You're spending more than you earn. Focus on increasing income or reducing expenses")
        }
        
        // Category insights
        val expensesByCategory = transactions.filter { it.type == "expense" }
            .groupBy { it.category }
            .mapValues { it.value.sumOf { transaction -> transaction.amount } }
        
        val topExpenseCategory = expensesByCategory.maxByOrNull { it.value }
        topExpenseCategory?.let { (category, amount) ->
            val percentage = (amount / totalExpense * 100).toInt()
            insights.add("${category} is your biggest expense category at ${percentage}% of total spending")
        }
        
        // Recent spending pattern
        val recentTransactions = transactions.filter { it.type == "expense" }
            .sortedByDescending { it.timestamp }
            .take(10)
        
        if (recentTransactions.size >= 5) {
            val recentAverage = recentTransactions.take(5).map { it.amount }.average()
            val olderAverage = recentTransactions.drop(5).map { it.amount }.average()
            
            when {
                recentAverage > olderAverage * 1.2 -> insights.add("Your recent spending has increased by ${((recentAverage - olderAverage) / olderAverage * 100).toInt()}%")
                recentAverage < olderAverage * 0.8 -> insights.add("Great! You've reduced spending by ${((olderAverage - recentAverage) / olderAverage * 100).toInt()}% recently")
                else -> insights.add("Your spending pattern has been consistent recently")
            }
        }
        
        // Income insights
        val incomeTransactions = transactions.filter { it.type == "income" }
        if (incomeTransactions.isNotEmpty()) {
            val incomeByCategory = incomeTransactions.groupBy { it.category }
                .mapValues { it.value.sumOf { transaction -> transaction.amount } }
            
            val topIncomeSource = incomeByCategory.maxByOrNull { it.value }
            topIncomeSource?.let { (category, amount) ->
                val percentage = (amount / totalIncome * 100).toInt()
                insights.add("${category} contributes ${percentage}% of your total income")
            }
        }
        
        _insights.value = insights
    }

    private fun generateDemoData() {
        // Generate demo data for when API fails
        val demoMonthly = listOf(
            MonthlyDataPoint("Oct 2024", 15000.0, 12000.0),
            MonthlyDataPoint("Nov 2024", 18000.0, 14000.0),
            MonthlyDataPoint("Dec 2024", 16000.0, 13500.0),
            MonthlyDataPoint("Jan 2025", 17000.0, 12500.0),
            MonthlyDataPoint("Feb 2025", 19000.0, 15000.0),
            MonthlyDataPoint("Mar 2025", 20000.0, 14500.0)
        )
        
        val demoCategories = listOf(
            CategoryDataPoint("Food & Grocery", 8000.0, Green600),
            CategoryDataPoint("Transport", 3000.0, Blue600),
            CategoryDataPoint("Medical", 2000.0, Red600),
            CategoryDataPoint("Utilities", 1500.0, Orange600),
            CategoryDataPoint("Other", 1000.0, Gray600)
        )
        
        val demoInsights = listOf(
            "You're saving 25% of your income - that's excellent!",
            "Food & Grocery is your biggest expense at 53% of spending",
            "Your spending has been consistent over the last month",
            "Farm Income contributes 70% of your total income"
        )
        
        _monthlyData.value = demoMonthly
        _categoryBreakdown.value = demoCategories
        _insights.value = demoInsights
    }

    fun refreshData() {
        loadAnalytics()
    }
}