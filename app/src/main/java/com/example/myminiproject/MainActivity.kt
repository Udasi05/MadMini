package com.example.myminiproject

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myminiproject.data.preferences.PreferencesManager
import com.example.myminiproject.navigation.NavGraph
import com.example.myminiproject.ui.theme.MyminiprojectTheme
import com.example.myminiproject.utils.SessionManager
import com.google.firebase.FirebaseApp
import java.util.Locale
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager

    // Runtime permission launcher for notifications
    private val notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                    isGranted: Boolean ->
                // Notifications will only work if permission is granted
                // No specific action needed here, the app handles both cases
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize session manager
        sessionManager = (application as DhanSathiApplication).sessionManager

        // Request notification permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Apply saved language preference
        applyLanguagePreference()

        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            MyminiprojectTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavGraph(sessionManager = sessionManager)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Update session activity when app becomes active
        sessionManager.updateLastActivity()
    }

    private fun applyLanguagePreference() {
        val preferencesManager = PreferencesManager(this)
        runBlocking {
            val languageCode = preferencesManager.languageFlow.first()
            setAppLocale(this@MainActivity, languageCode)
        }
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
