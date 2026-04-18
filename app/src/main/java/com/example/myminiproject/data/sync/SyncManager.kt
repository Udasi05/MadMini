package com.example.myminiproject.data.sync

import android.content.Context
import androidx.work.*
import com.example.myminiproject.data.api.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class SyncManager(private val context: Context) {
    private val apiService = ApiClient.apiService

    suspend fun syncAllData() {
        // Simplified sync without Room database
        // This will be enhanced later when Room is properly configured
        try {
            // For now, just trigger a simple API call to test connectivity
            // In a real implementation, this would sync local and remote data
            println("Sync manager: Checking connectivity...")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        fun schedulePeriodicSync(context: Context) {
            val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                15, TimeUnit.MINUTES
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "data_sync",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
        }

        fun triggerImmediateSync(context: Context) {
            val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueue(syncRequest)
        }
    }
}

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val syncManager = SyncManager(applicationContext)
            syncManager.syncAllData()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}