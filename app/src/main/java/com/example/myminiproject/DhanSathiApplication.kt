package com.example.myminiproject

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.myminiproject.data.local.AppDatabase
import com.example.myminiproject.data.sync.SyncManager
import com.example.myminiproject.utils.ActivityLifecycleObserver
import com.example.myminiproject.utils.SessionManager

class DhanSathiApplication : Application() {
    
    lateinit var sessionManager: SessionManager
        private set
    
    lateinit var database: AppDatabase
        private set
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Room database
        database = AppDatabase.getDatabase(this)
        
        // Initialize session manager
        sessionManager = SessionManager(this)
        
        // Register lifecycle observer for session management
        val lifecycleObserver = ActivityLifecycleObserver(sessionManager)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
        
        // Initialize periodic sync
        SyncManager.schedulePeriodicSync(this)
        
        println("DhanSathi Application initialized with Room database & session management")
    }
    
    companion object {
        fun getDatabase(application: Application): AppDatabase {
            return (application as DhanSathiApplication).database
        }
    }
}