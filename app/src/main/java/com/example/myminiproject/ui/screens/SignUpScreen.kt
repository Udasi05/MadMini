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
import androidx.compose.material.icons.filled.Person
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
fun SignUpScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(false) }
    val isValid = name.isNotBlank() && phone.length == 10

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
                    Box(
                        modifier = Modifier.size(80.dp).clip(CircleShape).background(Blue50),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Blue600, modifier = Modifier.size(36.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Create your account", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Slate800)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tell us about yourself to get started", fontSize = 15.sp, color = Gray500, textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(32.dp))

                    // Name Input
                    Text("Your Name", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Gray700, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("Enter your name") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue600,
                            unfocusedBorderColor = Gray200,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Input
                    Text("Phone Number", fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Gray700, modifier = Modifier.fillMaxWidth())
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
                            placeholder = { Text("Enter 10-digit number") },
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

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (isValid) {
                                loading = true
                                scope.launch {
                                    delay(1000)
                                    loading = false
                                    navController.navigate(Screen.OTP.createRoute(phone, name, "signup"))
                                }
                            }
                        },
                        enabled = isValid && !loading,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isValid) Blue600 else Blue400,
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
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        Text("Already have an account? ", fontSize = 14.sp, color = Gray500)
                        TextButton(onClick = { navController.navigate(Screen.Login.route) }) {
                            Text("Log in", fontWeight = FontWeight.SemiBold, color = Blue600, fontSize = 14.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Gray400, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Secure & Encrypted", fontSize = 12.sp, color = Gray400)
                    }
                }
            }
        }
    }
}
