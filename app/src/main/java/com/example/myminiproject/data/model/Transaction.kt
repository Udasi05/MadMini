package com.example.myminiproject.data.model

data class Transaction(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val type: String = "income", // "income" or "expense"
    val category: String = "",
    val icon: String = "💰",
    val date: String = "",
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        val incomeCategories = listOf("Farm Income", "Daily Wage", "Business", "Other Income")
        val expenseCategories = listOf("Food & Grocery", "Transport", "Medical", "Education", "Utilities", "Other")

        val categoryIcons = mapOf(
            "Farm Income" to "🌾",
            "Daily Wage" to "⚒️",
            "Business" to "💼",
            "Other Income" to "💵",
            "Food & Grocery" to "🛒",
            "Transport" to "🚌",
            "Medical" to "💊",
            "Education" to "📚",
            "Utilities" to "📱",
            "Other" to "💸"
        )

        fun getSampleTransactions(): List<Transaction> = listOf(
            Transaction(id = "1", title = "Farm Sale - Wheat", amount = 4200.0, type = "income", category = "Farm Income", icon = "🌾", date = "Today, 10:30 AM"),
            Transaction(id = "2", title = "Grocery", amount = 380.0, type = "expense", category = "Food & Grocery", icon = "🛒", date = "Today, 8:15 AM"),
            Transaction(id = "3", title = "Daily Wage", amount = 600.0, type = "income", category = "Daily Wage", icon = "⚒️", date = "Yesterday"),
            Transaction(id = "4", title = "Mobile Recharge", amount = 199.0, type = "expense", category = "Utilities", icon = "📱", date = "Yesterday"),
            Transaction(id = "5", title = "Milk Sale", amount = 350.0, type = "income", category = "Farm Income", icon = "🥛", date = "2 days ago"),
            Transaction(id = "6", title = "Medical", amount = 250.0, type = "expense", category = "Medical", icon = "💊", date = "2 days ago"),
            Transaction(id = "7", title = "Wage Payment", amount = 800.0, type = "income", category = "Daily Wage", icon = "💰", date = "3 days ago"),
            Transaction(id = "8", title = "School Fees", amount = 500.0, type = "expense", category = "Education", icon = "📚", date = "3 days ago"),
        )
    }
}
