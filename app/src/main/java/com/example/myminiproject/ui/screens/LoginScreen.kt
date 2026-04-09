package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController) {
    var phone by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    Column(
        modifier = Modifier.fillMaxSize().background(Gray50)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(Gray100).size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Gray600, modifier = Modifier.size(20.dp))
            }
            Box(
                modifier = Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(Blue600),
                contentAlignment = Alignment.Center
            ) {
                Text("DS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Text("DhanSathi", fontWeight = FontWeight.SemiBold, color = Blue900)
        }

        // Content
        Column(
            modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 20 }) + fadeIn(tween(500))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // Icon
                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(Blue50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = Blue600, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Welcome Back",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate800
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Enter your phone number to login",
                        fontSize = 15.sp,
                        color = Gray500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Phone Input
                    Text(
                        text = "Phone Number",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Gray700,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .border(1.dp, Gray200, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                        ) {
                            Text("+91", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Gray600)
                        }
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
                            placeholder = { Text("Enter 10-digit mobile number", fontSize = 15.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Blue600,
                                unfocusedBorderColor = Gray200,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We'll send you an OTP for verification",
                        fontSize = 12.sp,
                        color = Gray400,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Send OTP Button
                    Button(
                        onClick = {
                            if (phone.length == 10) {
                                loading = true
                                scope.launch {
                                    delay(1000)
                                    loading = false
                                    navController.navigate(Screen.OTP.createRoute(phone, "", "login"))
                                }
                            }
                        },
                        enabled = phone.length == 10 && !loading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (phone.length == 10) Blue600 else Blue400,
                            disabledContainerColor = Blue400.copy(alpha = 0.5f)
                        )
                    ) {
                        if (loading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sending OTP...", color = Color.White, fontWeight = FontWeight.SemiBold)
                        } else {
                            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send OTP", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign up link
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("Don't have an account? ", fontSize = 14.sp, color = Gray500)
                        TextButton(onClick = { navController.navigate(Screen.SignUp.route) }) {
                            Text("Sign Up", fontWeight = FontWeight.SemiBold, color = Blue600, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Security note
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Gray400, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Secure login with government-backed security", fontSize = 12.sp, color = Gray400)
                    }
                }
            }
        }
    }
}
