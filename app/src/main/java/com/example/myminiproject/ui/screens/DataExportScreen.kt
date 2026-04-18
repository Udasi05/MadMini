package com.example.myminiproject.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataExportScreen(navController: NavController) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        // Top Bar
        TopAppBar(
            title = { Text("Data Export", fontWeight = FontWeight.SemiBold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Export Your Data",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Gray800
            )
            
            // Simple Share Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        shareSimpleText(context, "This is a test export from DhanSathi app!")
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Blue600.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            tint = Blue600,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Test Export",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Gray800
                        )
                        Text(
                            "Share a test message to verify export works",
                            fontSize = 13.sp,
                            color = Gray600,
                            lineHeight = 18.sp
                        )
                    }
                    
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Gray400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            // Account Info Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        shareAccountInfo(context)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Green600.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Green600,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Share Account Info",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Gray800
                        )
                        Text(
                            "Share basic account information",
                            fontSize = 13.sp,
                            color = Gray600,
                            lineHeight = 18.sp
                        )
                    }
                    
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Gray400,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                "Note: This is a simplified export feature.",
                fontSize = 12.sp,
                color = Gray500,
                lineHeight = 16.sp
            )
        }
    }
}

private fun shareSimpleText(context: Context, text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_SUBJECT, "DhanSathi Export")
    }
    
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

private fun shareAccountInfo(context: Context) {
    val accountInfo = """
    📱 DhanSathi Account Info
    
    👨‍🌾 User: Farmer
    📊 App: DhanSathi - Farming Finance
    📅 Date: ${java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date())}
    
    🌾 This app helps farmers manage their finances effectively.
    
    Generated by DhanSathi App
    """.trimIndent()
    
    shareSimpleText(context, accountInfo)
}