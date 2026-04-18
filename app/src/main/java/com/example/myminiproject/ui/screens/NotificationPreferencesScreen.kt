package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.data.preferences.PreferencesManager
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.utils.ToastHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPreferencesScreen(navController: NavController) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scope = rememberCoroutineScope()
    
    val schemeUpdates by preferencesManager.schemeUpdatesFlow.collectAsState(initial = true)
    val transactionReminders by preferencesManager.transactionRemindersFlow.collectAsState(initial = true)
    val learningTips by preferencesManager.learningTipsFlow.collectAsState(initial = true)
    val appUpdates by preferencesManager.appUpdatesFlow.collectAsState(initial = true)
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray50)
        ) {
            // Top Bar
            TopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.notification_preferences), 
                        fontWeight = FontWeight.SemiBold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Gray800
                )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Blue50),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Blue600,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "Manage your notification preferences to stay updated on important information.",
                            fontSize = 13.sp,
                            color = Blue800,
                            lineHeight = 18.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Notification Preferences
                NotificationPreferenceItem(
                    icon = Icons.Default.Campaign,
                    title = stringResource(R.string.scheme_updates),
                    description = "Get notified about new government schemes",
                    checked = schemeUpdates,
                    onCheckedChange = { 
                        scope.launch { 
                            preferencesManager.setSchemeUpdates(it)
                            ToastHelper.showNotificationToggled(context, it)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                NotificationPreferenceItem(
                    icon = Icons.Default.Receipt,
                    title = stringResource(R.string.transaction_reminders),
                    description = "Reminders to track your daily transactions",
                    checked = transactionReminders,
                    onCheckedChange = { 
                        scope.launch { 
                            preferencesManager.setTransactionReminders(it)
                            ToastHelper.showNotificationToggled(context, it)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                NotificationPreferenceItem(
                    icon = Icons.Default.Lightbulb,
                    title = stringResource(R.string.learning_tips),
                    description = "Financial tips and farming advice",
                    checked = learningTips,
                    onCheckedChange = { 
                        scope.launch { 
                            preferencesManager.setLearningTips(it)
                            ToastHelper.showNotificationToggled(context, it)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                NotificationPreferenceItem(
                    icon = Icons.Default.SystemUpdate,
                    title = stringResource(R.string.app_updates),
                    description = "New features and app improvements",
                    checked = appUpdates,
                    onCheckedChange = { 
                        scope.launch { 
                            preferencesManager.setAppUpdates(it)
                            ToastHelper.showNotificationToggled(context, it)
                        }
                    }
                )
                
                Spacer(modifier = Modifier.height(80.dp)) // Add space for the bottom card
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Green600)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Text(
                        stringResource(R.string.preferences_saved),
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationPreferenceItem(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (checked) Blue100 else Gray100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (checked) Blue600 else Gray400,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Gray800
                )
                Text(
                    description,
                    fontSize = 12.sp,
                    color = Gray500
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Blue600,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Gray300
                )
            )
        }
    }
}
