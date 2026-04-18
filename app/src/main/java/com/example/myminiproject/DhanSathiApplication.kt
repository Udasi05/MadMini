package com.example.myminiproject

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.myminiproject.data.sync.SyncManager
import com.example.myminiproject.utils.ActivityLifecycleObserver
import com.example.myminiproject.utils.SessionManager

class DhanSathiApplication : Application() {
    
    lateinit var sessionManager: SessionManager
        private set
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        
        // Initialize session manager
        sessionManager = SessionManager(this)
        
        // Register lifecycle observer for session management
        val lifecycleObserver = ActivityLifecycleObserver(sessionManager)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
        
        // Initialize periodic sync
        SyncManager.schedulePeriodicSync(this)
        
        println("DhanSathi Application initialized with session management")
    }
}