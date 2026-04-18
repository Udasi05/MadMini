package com.example.myminiproject.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class ActivityLifecycleObserver(
    private val sessionManager: SessionManager
) : DefaultLifecycleObserver {
    
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // App came to foreground - check session validity
        sessionManager.checkSessionValidity()
        println("App resumed - checking session")
    }
    
    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        // User is actively using the app - update activity
        sessionManager.updateLastActivity()
        println("App active - updating session activity")
    }
    
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        // App going to background - update last activity time
        sessionManager.updateLastActivity()
        println("App paused - session will remain active for ${sessionManager.getSessionTimeRemainingMinutes()} minutes")
    }
    
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // App fully in background - session timer starts counting
        println("App stopped - session timeout in ${sessionManager.getSessionTimeRemainingMinutes()} minutes")
    }
}