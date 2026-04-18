package com.example.myminiproject.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.myminiproject.services.NotificationSchedulerService

object NotificationScheduler {
    
    fun startPeriodicNotifications(context: Context) {
        if (!NotificationSchedulerService.isRunning()) {
            val intent = Intent(context, NotificationSchedulerService::class.java)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            
            println("Periodic notifications started")
        } else {
            println("Notification service already running")
        }
    }
    
    fun stopPeriodicNotifications(context: Context) {
        val intent = Intent(context, NotificationSchedulerService::class.java)
        context.stopService(intent)
        println("Periodic notifications stopped")
    }
    
    fun isNotificationServiceRunning(): Boolean {
        return NotificationSchedulerService.isRunning()
    }
}