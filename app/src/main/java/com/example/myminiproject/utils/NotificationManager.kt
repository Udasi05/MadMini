package com.example.myminiproject.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myminiproject.MainActivity
import com.example.myminiproject.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "dhansathi_notifications"
        const val CHANNEL_NAME = "DhanSathi Notifications"
        const val NOTIFICATION_ID_TRANSACTION = 1001
        const val NOTIFICATION_ID_SAVINGS = 1002
        const val NOTIFICATION_ID_SCHEME = 1003
        const val NOTIFICATION_ID_TIPS = 1004
        const val NOTIFICATION_ID_REMINDER = 1005
        const val NOTIFICATION_ID_ENGAGEMENT = 1006
        const val NOTIFICATION_ID_EXPENSE = 2001
        const val NOTIFICATION_ID_INCOME = 2002
        const val NOTIFICATION_ID_PROFILE = 2003
        const val NOTIFICATION_ID_LOGIN = 2004
        const val NOTIFICATION_ID_OTP = 2005
        const val NOTIFICATION_ID_LOGOUT = 2006
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                    NotificationChannel(
                                    CHANNEL_ID,
                                    CHANNEL_NAME,
                                    NotificationManager.IMPORTANCE_DEFAULT
                            )
                            .apply {
                                description =
                                        "Notifications for DhanSathi app updates and reminders"
                            }

            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun showTransactionReminder() {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Track Your Money")
                        .setContentText("Don't forget to add today's transactions!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TRANSACTION, notification)
    }

    fun showSavingsGoal(amount: Double) {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Savings Milestone!")
                        .setContentText(
                                "Great! You've saved ₹${String.format("%,.0f", amount)} this month"
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SAVINGS, notification)
    }

    fun showSchemeReminder() {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("New Government Schemes")
                        .setContentText("Check out new schemes you might be eligible for")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SCHEME, notification)
    }

    fun showFinancialTip() {
        val tips =
                listOf(
                        "💡 Tip: Save at least 10% of your income every month",
                        "🌾 Farm Tip: Track seasonal income patterns for better planning",
                        "💰 Money Tip: Use voice input to quickly add transactions",
                        "📊 Smart Tip: Check your weekly spending trends in dashboard",
                        "🎯 Goal Tip: Set monthly savings targets to build wealth",
                        "📱 App Tip: Use schemes section to find government benefits",
                        "💡 Finance Tip: Emergency fund should cover 3-6 months expenses",
                        "🌱 Growth Tip: Invest surplus income in safe government schemes",
                        "📈 Track Tip: Regular expense tracking helps identify savings",
                        "🔔 Reminder: Update your transactions daily for accuracy"
                )

        val randomTip = tips.random()
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("DhanSathi Financial Tip")
                        .setContentText(randomTip)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(randomTip))
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TIPS, notification)
    }

    fun showEngagementNotification() {
        val messages =
                listOf(
                        "📱 Your financial journey awaits! Check your progress in DhanSathi",
                        "🌾 How's your farming income this week? Track it in DhanSathi",
                        "💰 Quick check: Have you added today's expenses?",
                        "📊 Your weekly financial summary is ready to view",
                        "🎯 Stay on track with your savings goals in DhanSathi",
                        "🔍 Discover new government schemes that benefit you",
                        "📈 See how your financial habits are improving",
                        "💡 Learn something new in our finance education section",
                        "🌱 Your financial growth starts with small daily steps",
                        "📱 DhanSathi: Making rural finance simple and smart"
                )

        val randomMessage = messages.random()
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("DhanSathi")
                        .setContentText(randomMessage)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(randomMessage))
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_ENGAGEMENT, notification)
    }

    fun showDailyReminder() {
        val reminders =
                listOf(
                        "🌅 Good morning! Start your day by checking your finances",
                        "☀️ Afternoon check: How are your expenses today?",
                        "🌙 Evening review: Add today's transactions before bed",
                        "📝 Daily habit: Track your money to build wealth",
                        "💪 Stay consistent: Small daily actions lead to big results",
                        "🎯 Daily goal: Add at least one transaction today",
                        "📊 Quick update: How's your weekly budget looking?",
                        "💡 Daily wisdom: Every rupee saved is a rupee earned",
                        "🌱 Daily growth: Your financial discipline is building wealth",
                        "📱 Daily reminder: DhanSathi is here to help your finances"
                )

        val randomReminder = reminders.random()
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Daily Financial Check")
                        .setContentText(randomReminder)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(randomReminder))
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_REMINDER, notification)
    }

    fun showRandomNotification() {
        val notificationTypes =
                listOf(
                        { showFinancialTip() },
                        { showEngagementNotification() },
                        { showDailyReminder() },
                        { showTransactionReminder() }
                )

        // Randomly select and show a notification
        notificationTypes.random().invoke()
    }

    // ==================== TRANSACTION NOTIFICATIONS ====================

    /**
     * Show notification when expense is added
     * @param amount Expense amount
     * @param category Expense category (e.g., "Food", "Transport", "Medical")
     */
    fun showExpenseAdded(amount: Double, category: String = "Expense") {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("💸 Expense Added")
                        .setContentText(
                                "₹${String.format("%,.2f", amount)} $category added to your account"
                        )
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "Expense of ₹${String.format("%,.2f", amount)} ($category) has been added successfully"
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_EXPENSE, notification)
    }

    /**
     * Show notification when income is added
     * @param amount Income amount
     * @param source Income source (e.g., "Salary", "Farm Income", "Business")
     */
    fun showIncomeAdded(amount: Double, source: String = "Income") {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("💰 Income Added")
                        .setContentText("₹${String.format("%,.2f", amount)} from $source received!")
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "Income of ₹${String.format("%,.2f", amount)} from $source has been added to your account"
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_INCOME, notification)
    }

    /**
     * Show notification for transaction summary
     * @param expenses Total expenses
     * @param income Total income
     * @param balance Net balance
     */
    fun showTransactionSummary(expenses: Double, income: Double, balance: Double) {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("📊 Daily Summary")
                        .setContentText(
                                "Income: ₹${String.format("%,.2f", income)} | Expense: ₹${String.format("%,.2f", expenses)}"
                        )
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "Daily Summary:\n" +
                                                        "💰 Income: ₹${String.format("%,.2f", income)}\n" +
                                                        "💸 Expense: ₹${String.format("%,.2f", expenses)}\n" +
                                                        "💵 Balance: ₹${String.format("%,.2f", balance)}"
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TRANSACTION, notification)
    }

    // ==================== PROFILE NOTIFICATIONS ====================

    /** Show notification when user profile is updated */
    fun showProfileUpdated(userName: String? = null) {
        val title = if (userName != null) "Welcome, $userName!" else "Profile Updated"
        val text = "Your profile has been successfully updated"

        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("👤 $title")
                        .setContentText(text)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_PROFILE, notification)
    }

    // ==================== AUTHENTICATION NOTIFICATIONS ====================

    /** Show notification on successful login */
    fun showLoginSuccess(userName: String? = null) {
        val greeting =
                when {
                    userName != null -> "Welcome back, $userName!"
                    else -> "Login Successful! 🎉"
                }

        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("✅ Login Successful")
                        .setContentText(greeting)
                        .setStyle(NotificationCompat.BigTextStyle().bigText(greeting))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_LOGIN, notification)
    }

    /** Show notification on logout */
    fun showLogoutSuccess() {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("👋 Logged Out")
                        .setContentText("You have been successfully logged out. Stay safe!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_LOGOUT, notification)
    }

    /** Show notification when OTP is received */
    fun showOtpReceived(phone: String) {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("🔐 OTP Received")
                        .setContentText("OTP sent to $phone. Enter it to verify your account.")
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "One-Time Password (OTP) has been sent to $phone. Use it to verify your identity."
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_OTP, notification)
    }

    /** Show notification when OTP is verified */
    fun showOtpVerified() {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("✅ OTP Verified")
                        .setContentText("Your account has been verified successfully!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_OTP, notification)
    }

    // ==================== SAVINGS & GOALS NOTIFICATIONS ====================

    /** Show notification for savings goal achievement */
    fun showSavingsGoalAchieved(goalName: String, amount: Double) {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("🎯 Goal Achieved!")
                        .setContentText(
                                "Congratulations! You've reached ₹${String.format("%,.2f", amount)} for $goalName"
                        )
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "🎉 Congratulations!\nYou've successfully achieved your savings goal of ₹${String.format("%,.2f", amount)} for $goalName"
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SAVINGS, notification)
    }

    /** Show notification for savings goal progress */
    fun showSavingsGoalProgress(
            goalName: String,
            percentage: Int,
            currentAmount: Double,
            targetAmount: Double
    ) {
        val progressText = "Progress: $percentage% complete"

        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("📈 $goalName - $percentage%")
                        .setContentText(
                                "₹${String.format("%,.2f", currentAmount)} of ₹${String.format("%,.2f", targetAmount)}"
                        )
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "$goalName Progress:\n" +
                                                        "Current: ₹${String.format("%,.2f", currentAmount)}\n" +
                                                        "Target: ₹${String.format("%,.2f", targetAmount)}\n" +
                                                        "$progressText"
                                        )
                        )
                        .setProgress(100, percentage, false)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SAVINGS, notification)
    }

    // ==================== SCHEME NOTIFICATIONS ====================

    /** Show notification for new scheme eligibility */
    fun showSchemeEligibility(schemeName: String, benefits: String) {
        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("🎁 New Scheme Available")
                        .setContentText("You might be eligible for $schemeName")
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "You might be eligible for the $schemeName scheme!\n\nBenefits: $benefits\n\nTap to learn more."
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_SCHEME, notification)
    }

    /** Show notification for budget warning */
    fun showBudgetWarning(category: String, spent: Double, limit: Double) {
        val remaining = limit - spent
        val percentage = ((spent / limit) * 100).toInt()

        val notification =
                NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("⚠️ Budget Alert")
                        .setContentText("$category: You've spent ${percentage}% of your budget")
                        .setStyle(
                                NotificationCompat.BigTextStyle()
                                        .bigText(
                                                "Budget Alert for $category:\n" +
                                                        "Spent: ₹${String.format("%,.2f", spent)}\n" +
                                                        "Limit: ₹${String.format("%,.2f", limit)}\n" +
                                                        "Remaining: ₹${String.format("%,.2f", remaining)}"
                                        )
                        )
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(createPendingIntent())
                        .setAutoCancel(true)
                        .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_TRANSACTION, notification)
    }
}
