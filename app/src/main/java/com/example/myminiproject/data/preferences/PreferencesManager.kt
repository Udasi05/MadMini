package com.example.myminiproject.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_preferences")

class PreferencesManager(private val context: Context) {
    
    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val NOTIFICATIONS_ENABLED_KEY = booleanPreferencesKey("notifications_enabled")
        private val SCHEME_UPDATES_KEY = booleanPreferencesKey("scheme_updates")
        private val TRANSACTION_REMINDERS_KEY = booleanPreferencesKey("transaction_reminders")
        private val LEARNING_TIPS_KEY = booleanPreferencesKey("learning_tips")
        private val APP_UPDATES_KEY = booleanPreferencesKey("app_updates")
    }
    
    // Language preference
    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: "en" // Default to English
    }
    
    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }
    
    // Notification preferences
    val notificationsEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED_KEY] ?: true
    }
    
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED_KEY] = enabled
        }
    }
    
    val schemeUpdatesFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[SCHEME_UPDATES_KEY] ?: true
    }
    
    suspend fun setSchemeUpdates(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SCHEME_UPDATES_KEY] = enabled
        }
    }
    
    val transactionRemindersFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[TRANSACTION_REMINDERS_KEY] ?: true
    }
    
    suspend fun setTransactionReminders(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[TRANSACTION_REMINDERS_KEY] = enabled
        }
    }
    
    val learningTipsFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LEARNING_TIPS_KEY] ?: true
    }
    
    suspend fun setLearningTips(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LEARNING_TIPS_KEY] = enabled
        }
    }
    
    val appUpdatesFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[APP_UPDATES_KEY] ?: true
    }
    
    suspend fun setAppUpdates(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[APP_UPDATES_KEY] = enabled
        }
    }
}
