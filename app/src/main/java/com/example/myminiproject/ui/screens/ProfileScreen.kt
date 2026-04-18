package com.example.myminiproject.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.ProfileViewModel
import com.example.myminiproject.ui.viewmodels.TransactionViewModel
import com.example.myminiproject.utils.NotificationHelper
import com.example.myminiproject.utils.NotificationScheduler
import com.example.myminiproject.utils.SessionManager
import com.example.myminiproject.utils.ToastHelper
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
        navController: NavController,
        sessionManager: SessionManager,
        viewModel: ProfileViewModel = viewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    val preferencesManager = remember {
        com.example.myminiproject.data.preferences.PreferencesManager(context)
    }
    val scope = rememberCoroutineScope()
    val transactionViewModel: TransactionViewModel = viewModel()

    val currentLanguage by preferencesManager.languageFlow.collectAsState(initial = "en")
    var showLangPicker by remember { mutableStateOf(false) }
    val notificationsOn by
            preferencesManager.notificationsEnabledFlow.collectAsState(initial = true)

    val profileData by viewModel.profileData.collectAsState()
    val transactions by transactionViewModel.transactions.collectAsState()
    val stats by transactionViewModel.stats.collectAsState()

    val fmt = NumberFormat.getNumberInstance(Locale("en", "IN"))

    // Language display mapping
    val languageDisplayMap =
            mapOf("en" to "English", "hi" to "हिंदी (Hindi)", "mr" to "मराठी (Marathi)")

    val languageCodeMap =
            mapOf("English" to "en", "हिंदी (Hindi)" to "hi", "मराठी (Marathi)" to "mr")

    val selectedLangDisplay = languageDisplayMap[currentLanguage] ?: "English"
    val languages = listOf("English", "हिंदी (Hindi)", "मराठी (Marathi)")

    // Load profile and transactions on first composition
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        transactionViewModel.loadTransactions()
    }

    Column(
            modifier =
                    Modifier.fillMaxSize().background(Gray50).verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(
                                        brush =
                                                Brush.linearGradient(
                                                        colors = listOf(Blue900, Blue600),
                                                        start = Offset(0f, 0f),
                                                        end =
                                                                Offset(
                                                                        Float.POSITIVE_INFINITY,
                                                                        Float.POSITIVE_INFINITY
                                                                )
                                                )
                                )
                                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 24.dp)
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        stringResource(R.string.profile),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                )
                IconButton(
                        onClick = { navController.navigate(Screen.EditProfile.route) },
                        modifier =
                                Modifier.clip(RoundedCornerShape(12.dp))
                                        .background(Color.White.copy(alpha = 0.15f))
                                        .size(40.dp)
                ) {
                    Icon(
                            Icons.Default.Edit,
                            contentDescription = stringResource(R.string.edit),
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar & Name
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                        modifier =
                                Modifier.size(64.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                ) {
                    Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                    )
                }
                Column {
                    val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
                    val userName = user?.displayName.takeIf { !it.isNullOrBlank() } ?: "User"
                    val userPhone = user?.phoneNumber ?: "No phone registered"

                    Text(
                            userName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                    )
                    Text(userPhone, fontSize = 13.sp, color = Blue200)
                }
            }
        }

        // Stats
        Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).offset(y = (-16).dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn(Icons.Default.Receipt, "${transactions.size}", "Transactions")
                StatColumn(
                        Icons.Default.Savings,
                        "₹${fmt.format(stats.balance.toLong())}",
                        "Balance"
                )
                StatColumn(Icons.Default.School, "0/5", "Lessons Done")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Account Menu
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val userName = user?.displayName.takeIf { !it.isNullOrBlank() } ?: "User"
        val userDetails =
                profileData?.let {
                    "${it.state}${if (it.landHolding > 0) " • ${it.landHolding} ha" else ""}"
                }
                        ?: stringResource(R.string.tap_to_add_details)

        MenuSection(
                title = stringResource(R.string.account),
                items =
                        listOf(
                                MenuItem(
                                        Icons.Outlined.Person,
                                        stringResource(R.string.edit_profile),
                                        userDetails
                                ) { navController.navigate(Screen.EditProfile.route) },
                                MenuItem(
                                        Icons.Outlined.Notifications,
                                        stringResource(R.string.notifications),
                                        if (notificationsOn) "Enabled • Every 3-5 minutes"
                                        else stringResource(R.string.disabled),
                                        toggle = true,
                                        toggled = notificationsOn
                                ) {
                                    scope.launch {
                                        preferencesManager.setNotificationsEnabled(!notificationsOn)
                                        if (!notificationsOn) {
                                            // Start notifications when enabled
                                            NotificationScheduler.startPeriodicNotifications(
                                                    context
                                            )
                                        } else {
                                            // Stop notifications when disabled
                                            NotificationScheduler.stopPeriodicNotifications(context)
                                        }
                                    }
                                },
                                MenuItem(
                                        Icons.Outlined.Language,
                                        stringResource(R.string.language),
                                        selectedLangDisplay
                                ) { showLangPicker = true }
                        )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Support Menu
        MenuSection(
                title = stringResource(R.string.support),
                items =
                        listOf(
                                MenuItem(
                                        Icons.Outlined.HelpOutline,
                                        stringResource(R.string.help_support),
                                        stringResource(R.string.faq_contact)
                                ) { navController.navigate(Screen.HelpSupport.route) },
                                MenuItem(
                                        Icons.Outlined.Download,
                                        "Data Export & Backup",
                                        "Export your data"
                                ) { navController.navigate(Screen.DataExport.route) },
                                MenuItem(
                                        Icons.Outlined.Star,
                                        stringResource(R.string.rate_app),
                                        stringResource(R.string.tell_us)
                                ) {}
                        )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // App Info
        Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                        modifier =
                                Modifier.size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Blue600),
                        contentAlignment = Alignment.Center
                ) {
                    Text("DS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                        "DhanSathi",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray700
                )
                Text(
                        "Version 1.0.0 • A Digital India Initiative",
                        fontSize = 11.sp,
                        color = Gray400
                )
                Text("Gaon ki kamayi, Shahar ki Samajh", fontSize = 10.sp, color = Gray400)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        Button(
                onClick = {
                    // Clear session and Firebase auth
                    sessionManager.clearSession()
                    com.google.firebase.auth.FirebaseAuth.getInstance().signOut()
                    // Show logout success notification
                    notificationHelper.showLogoutSuccess()
                    ToastHelper.showLogoutSuccess(context)

                    navController.navigate(Screen.Splash.route) { popUpTo(0) { inclusive = true } }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Red100)
        ) {
            Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    tint = Red500,
                    modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                    stringResource(R.string.log_out),
                    color = Red500,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Language Picker
    if (showLangPicker) {
        LanguagePickerSheet(
                languages = languages,
                selected = selectedLangDisplay,
                onSelect = { displayName ->
                    val langCode = languageCodeMap[displayName] ?: "en"
                    scope.launch {
                        preferencesManager.setLanguage(langCode)
                        // Recreate activity to apply language change
                        (context as? android.app.Activity)?.recreate()
                    }
                    showLangPicker = false
                },
                onDismiss = { showLangPicker = false }
        )
    }
}

@Composable
private fun StatColumn(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Blue100),
                contentAlignment = Alignment.Center
        ) { Icon(icon, contentDescription = null, tint = Blue600, modifier = Modifier.size(16.dp)) }
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Gray800)
        Text(label, fontSize = 10.sp, color = Gray400)
    }
}

data class MenuItem(
        val icon: ImageVector,
        val label: String,
        val sub: String,
        val toggle: Boolean = false,
        val toggled: Boolean = false,
        val onClick: () -> Unit
)

@Composable
private fun MenuSection(title: String, items: List<MenuItem>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(
                title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray400,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    Row(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .clickable { item.onClick() }
                                            .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                                modifier =
                                        Modifier.size(36.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(Gray100),
                                contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                    item.icon,
                                    contentDescription = null,
                                    tint = Gray600,
                                    modifier = Modifier.size(18.dp)
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                    item.label,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray800
                            )
                            Text(item.sub, fontSize = 11.sp, color = Gray400)
                        }
                        if (item.toggle) {
                            Switch(
                                    checked = item.toggled,
                                    onCheckedChange = { item.onClick() },
                                    colors =
                                            SwitchDefaults.colors(
                                                    checkedThumbColor = Color.White,
                                                    checkedTrackColor = Blue600,
                                                    uncheckedThumbColor = Color.White,
                                                    uncheckedTrackColor = Gray300
                                            ),
                                    modifier = Modifier.height(24.dp)
                            )
                        } else {
                            Icon(
                                    Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = Gray400,
                                    modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (index < items.size - 1) {
                        HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Gray100
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguagePickerSheet(
        languages: List<String>,
        selected: String,
        onSelect: (String) -> Unit,
        onDismiss: () -> Unit
) {
    ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = Color.White
    ) {
        Column(
                modifier =
                        Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp)
        ) {
            Text(
                    stringResource(R.string.select_language),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            languages.forEach { lang ->
                val isSelected = selected == lang
                Card(
                        modifier =
                                Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                                    onSelect(lang)
                                },
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = if (isSelected) Blue50 else Gray50
                                ),
                        border =
                                if (isSelected)
                                        androidx.compose.foundation.BorderStroke(1.5.dp, Blue600)
                                else
                                        androidx.compose.foundation.BorderStroke(
                                                1.5.dp,
                                                Color.Transparent
                                        )
                ) {
                    Row(
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                                lang,
                                fontSize = 14.sp,
                                fontWeight =
                                        if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color = if (isSelected) Blue600 else Gray700
                        )
                        if (isSelected) {
                            Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Blue600,
                                    modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
