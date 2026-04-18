package com.example.myminiproject.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.myminiproject.utils.NotificationHelper
import com.example.myminiproject.utils.SessionManager
import kotlin.random.Random

class NotificationSchedulerService : Service() {
    
    private lateinit var notificationHelper: NotificationHelper
    private lateinit var sessionManager: SessionManager
    private var handler: Handler? = null
    private var notificationRunnable: Runnable? = null
    
    companion object {
        private const val MIN_INTERVAL = 3 * 60 * 1000L // 3 minutes
        private const val MAX_INTERVAL = 5 * 60 * 1000L // 5 minutes
        private var isServiceRunning = false
        
        fun isRunning(): Boolean = isServiceRunning
    }
    
    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
        sessionManager = SessionManager(this)
        handler = Handler(Looper.getMainLooper())
        isServiceRunning = true
        
        println("NotificationSchedulerService created")
        scheduleNextNotification()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        println("NotificationSchedulerService started")
        return START_STICKY // Restart service if killed
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(notificationRunnable ?: return)
        isServiceRunning = false
        println("NotificationSchedulerService destroyed")
    }
    
    private fun scheduleNextNotification() {
        // Generate random interval between 3-5 minutes
        val randomInterval = Random.nextLong(MIN_INTERVAL, MAX_INTERVAL + 1)
        
        notificationRunnable = Runnable {
            // Only send notifications if user has a valid session (is logged in)
            if (sessionManager.checkSessionValidity()) {
                sendRandomNotification()
                println("Notification sent, next in ${randomInterval / 1000 / 60} minutes")
            } else {
                println("User not logged in, skipping notification")
            }
            
            // Schedule next notification
            scheduleNextNotification()
        }
        
        handler?.postDelayed(notificationRunnable!!, randomInterval)
        println("Next notification scheduled in ${randomInterval / 1000 / 60} minutes")
    }
    
    private fun sendRandomNotification() {
        try {
            // Check if notifications are enabled in app preferences
            val prefs = getSharedPreferences("dhansathi_session", MODE_PRIVATE)
            val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
            
            if (notificationsEnabled) {
                // Check DataStore preferences for more granular control
                val preferencesManager = com.example.myminiproject.data.preferences.PreferencesManager(this)
                
                // For now, send random notification - in future can check specific preferences
                notificationHelper.showRandomNotification()
                println("Random notification sent successfully")
            } else {
                println("Notifications disabled by user, skipping")
            }
        } catch (e: Exception) {
            println("Error sending notification: ${e.message}")
            e.printStackTrace()
        }
    }
}