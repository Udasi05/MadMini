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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*

@Composable
fun ProfileScreen(navController: NavController) {
    var selectedLang by remember { mutableStateOf("English") }
    var showLangPicker by remember { mutableStateOf(false) }
    var notificationsOn by remember { mutableStateOf(true) }

    val languages = listOf(
        "English", "हिंदी (Hindi)", "मराठी (Marathi)", "தமிழ் (Tamil)",
        "తెలుగు (Telugu)", "বাংলা (Bengali)", "ਪੰਜਾਬੀ (Punjabi)", "ಕನ್ನಡ (Kannada)"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Blue900, Blue600),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(start = 20.dp, end = 20.dp, top = 48.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Profile", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                IconButton(
                    onClick = {},
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.15f)).size(40.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar & Name
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👨‍🌾", fontSize = 28.sp)
                }
                Column {
                    Text("Ramesh Kumar", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("+91 98XX XXX X23", fontSize = 13.sp, color = Blue200)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Yellow300, modifier = Modifier.size(12.dp))
                        Text("Silver Member", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Yellow300)
                    }
                }
            }
        }

        // Stats
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-16).dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatColumn("📊", "28", "Transactions")
                StatColumn("💰", "₹4,200", "Savings")
                StatColumn("📚", "3/5", "Lessons Done")
            }
        }

        // Financial Score Banner
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(Amber500, Amber600))
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color.White, modifier = Modifier.size(22.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Financial Score: 68/100", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { 0.68f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
                Text("Good!", fontSize = 12.sp, color = Color.White.copy(alpha = 0.8f))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Account Menu
        MenuSection(title = "ACCOUNT", items = listOf(
            MenuItem(Icons.Outlined.Person, "Edit Profile", "Ramesh Kumar") { },
            MenuItem(Icons.Outlined.Notifications, "Notifications", if (notificationsOn) "Enabled" else "Disabled",
                toggle = true, toggled = notificationsOn) { notificationsOn = !notificationsOn },
            MenuItem(Icons.Outlined.Language, "Language", selectedLang) { showLangPicker = true }
        ))

        Spacer(modifier = Modifier.height(16.dp))

        // Support Menu
        MenuSection(title = "SUPPORT", items = listOf(
            MenuItem(Icons.Outlined.HelpOutline, "Help & Support", "FAQ, Contact us") { },
            MenuItem(Icons.Outlined.Shield, "Privacy & Security", "Government-backed security") { },
            MenuItem(Icons.Outlined.Star, "Rate the App", "Tell us what you think") { }
        ))

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
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Blue600),
                    contentAlignment = Alignment.Center
                ) {
                    Text("DS", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("DhanSathi", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Gray700)
                Text("Version 1.0.0 • A Digital India Initiative", fontSize = 11.sp, color = Gray400)
                Text("Gaon ki kamayi, Shahar ki Samajh", fontSize = 10.sp, color = Gray400)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout
        Button(
            onClick = {
                navController.navigate(Screen.Splash.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Red100)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null, tint = Red500, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Out", color = Red500, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Language Picker
    if (showLangPicker) {
        LanguagePickerSheet(
            languages = languages,
            selected = selectedLang,
            onSelect = { selectedLang = it; showLangPicker = false },
            onDismiss = { showLangPicker = false }
        )
    }
}

@Composable
private fun StatColumn(icon: String, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icon, fontSize = 24.sp)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { item.onClick() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(12.dp)).background(Gray100),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = null, tint = Gray600, modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Gray800)
                            Text(item.sub, fontSize = 11.sp, color = Gray400)
                        }
                        if (item.toggle) {
                            Switch(
                                checked = item.toggled,
                                onCheckedChange = { item.onClick() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Blue600,
                                    uncheckedThumbColor = Color.White,
                                    uncheckedTrackColor = Gray300
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        } else {
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Gray400, modifier = Modifier.size(16.dp))
                        }
                    }
                    if (index < items.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray100)
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Select Language", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            languages.forEach { lang ->
                val isSelected = selected == lang
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelect(lang) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Blue50 else Gray50
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Blue600)
                    else androidx.compose.foundation.BorderStroke(1.5.dp, Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            lang,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) Blue600 else Gray700
                        )
                        if (isSelected) {
                            Text("✓", fontSize = 16.sp, color = Blue600)
                        }
                    }
                }
            }
        }
    }
}
