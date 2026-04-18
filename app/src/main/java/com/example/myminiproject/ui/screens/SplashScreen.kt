package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.utils.SessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    sessionManager: SessionManager
) {
    var visible by remember { mutableStateOf(false) }
    var showButtons by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(400)
        
        // Check if user has a valid session
        if (sessionManager.checkSessionValidity()) {
            println("Valid session found, navigating to dashboard")
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        } else {
            println("No valid session, showing login options")
            showButtons = true
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(Blue900, Blue600, Blue500)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            AnimatedVisibility(
                visible = visible,
                enter = scaleIn(animationSpec = tween(600)) + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DS",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // App Name
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn(animationSpec = tween(600, delayMillis = 200))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "DhanSathi",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Gaon ki kamayi, Shahar ki Samajh",
                        fontSize = 15.sp,
                        color = Blue200
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Tagline
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn(animationSpec = tween(600, delayMillis = 400))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Empowering Rural India with Simple Finance",
                        fontSize = 18.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Track income • Manage expenses • Build financial security",
                        fontSize = 13.sp,
                        color = Blue200,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Feature pills
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 30 }) + fadeIn(animationSpec = tween(600, delayMillis = 600))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    val features = listOf("Voice Input", "12 Languages", "Smart Insights", "Gov Schemes")
                    features.forEach { feature ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = feature, fontSize = 11.sp, color = Color.White.copy(alpha = 0.9f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Stats
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 20 }) + fadeIn(animationSpec = tween(600, delayMillis = 800))
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem("10K+", "Users")
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(alpha = 0.2f)))
                    StatItem("12", "Languages")
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(Color.White.copy(alpha = 0.2f)))
                    StatItem("Free", "Forever")
                }
            }
        }

        // Bottom buttons
        AnimatedVisibility(
            visible = showButtons,
            enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(600))
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Get Started button
                androidx.compose.material3.Button(
                    onClick = { navController.navigate(Screen.SignUp.route) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Get Started Free →",
                        color = Blue700,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }

                // Login button
                androidx.compose.material3.OutlinedButton(
                    onClick = { navController.navigate(Screen.Login.route) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.35f)),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.White.copy(alpha = 0.15f)
                    )
                ) {
                    Text(
                        text = "Already have an account? Login",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                Text(
                    text = "Government-backed security standards",
                    fontSize = 11.sp,
                    color = Blue200,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text(text = label, fontSize = 12.sp, color = Blue200)
    }
}
