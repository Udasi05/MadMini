package com.example.myminiproject.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(private val context: Context) {
    
    companion object {
        private const val PREF_NAME = "dhansathi_session"
        private const val KEY_LAST_ACTIVITY = "last_activity_time"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val SESSION_TIMEOUT_HOURS = 2 // 2 hours session timeout
        private const val SESSION_TIMEOUT_MS = SESSION_TIMEOUT_HOURS * 60 * 60 * 1000L
    }
    
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    private val _isSessionValid = MutableStateFlow(false)
    val isSessionValid: StateFlow<Boolean> = _isSessionValid.asStateFlow()
    
    init {
        checkSessionValidity()
    }
    
    fun startSession(userId: String) {
        val currentTime = System.currentTimeMillis()
        prefs.edit().apply {
            putLong(KEY_LAST_ACTIVITY, currentTime)
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_USER_ID, userId)
            apply()
        }
        _isSessionValid.value = true
        println("Session started for user: $userId")
        
        // Start periodic notifications when session starts
        try {
            NotificationScheduler.startPeriodicNotifications(context)
        } catch (e: Exception) {
            println("Error starting notification service: ${e.message}")
        }
    }
    
    fun updateLastActivity() {
        if (isLoggedIn()) {
            val currentTime = System.currentTimeMillis()
            prefs.edit().apply {
                putLong(KEY_LAST_ACTIVITY, currentTime)
                apply()
            }
            println("Session activity updated")
        }
    }
    
    fun checkSessionValidity(): Boolean {
        val lastActivity = prefs.getLong(KEY_LAST_ACTIVITY, 0)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        val currentTime = System.currentTimeMillis()
        
        val isValid = if (isLoggedIn && lastActivity > 0) {
            val timeDiff = currentTime - lastActivity
            val isWithinTimeout = timeDiff < SESSION_TIMEOUT_MS
            
            if (!isWithinTimeout) {
                println("Session expired. Time since last activity: ${timeDiff / 1000 / 60} minutes")
                clearSession()
            } else {
                println("Session valid. Time since last activity: ${timeDiff / 1000 / 60} minutes")
            }
            
            isWithinTimeout
        } else {
            false
        }
        
        _isSessionValid.value = isValid
        return isValid
    }
    
    fun clearSession() {
        prefs.edit().apply {
            remove(KEY_LAST_ACTIVITY)
            remove(KEY_IS_LOGGED_IN)
            remove(KEY_USER_ID)
            apply()
        }
        _isSessionValid.value = false
        println("Session cleared")
        
        // Stop periodic notifications when session ends
        try {
            NotificationScheduler.stopPeriodicNotifications(context)
        } catch (e: Exception) {
            println("Error stopping notification service: ${e.message}")
        }
    }
    
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }
    
    fun getSessionTimeRemaining(): Long {
        val lastActivity = prefs.getLong(KEY_LAST_ACTIVITY, 0)
        if (lastActivity == 0L) return 0L
        
        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - lastActivity
        return maxOf(0L, SESSION_TIMEOUT_MS - elapsed)
    }
    
    fun getSessionTimeRemainingMinutes(): Int {
        return (getSessionTimeRemaining() / 1000 / 60).toInt()
    }
}
