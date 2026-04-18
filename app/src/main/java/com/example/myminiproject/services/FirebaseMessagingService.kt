package com.example.myminiproject.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.myminiproject.MainActivity
import com.example.myminiproject.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DhanSathiFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Handle FCM messages here
        remoteMessage.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "DhanSathi",
                body = notification.body ?: "",
                data = remoteMessage.data
            )
        }

        // Handle data payload
        if (remoteMessage.data.isNotEmpty()) {
            handleDataPayload(remoteMessage.data)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Send token to server
        sendTokenToServer(token)
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "dhansathi_notifications"

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "DhanSathi Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for DhanSathi app"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Create intent for notification tap
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Add extra data if needed
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun handleDataPayload(data: Map<String, String>) {
        // Handle different types of notifications based on data
        when (data["type"]) {
            "scheme_update" -> {
                // Handle scheme update notification
                showNotification(
                    title = "New Scheme Available",
                    body = data["message"] ?: "Check out new government schemes",
                    data = data
                )
            }
            "transaction_reminder" -> {
                // Handle transaction reminder
                showNotification(
                    title = "Transaction Reminder",
                    body = data["message"] ?: "Don't forget to log your daily transactions",
                    data = data
                )
            }
            "support_response" -> {
                // Handle support ticket response
                showNotification(
                    title = "Support Response",
                    body = data["message"] ?: "You have a new response to your support ticket",
                    data = data
                )
            }
            "financial_insight" -> {
                // Handle financial insight notification
                showNotification(
                    title = "Financial Insight",
                    body = data["message"] ?: "Check your latest financial insights",
                    data = data
                )
            }
        }
    }

    private fun sendTokenToServer(token: String) {
        // TODO: Send token to your backend server
        // This would typically be done via an API call
        println("FCM Token: $token")
    }
}