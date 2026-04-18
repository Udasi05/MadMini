package com.example.myminiproject.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myminiproject.R
import com.example.myminiproject.navigation.Screen
import com.example.myminiproject.ui.theme.*
import com.example.myminiproject.ui.viewmodels.AuthUiState
import com.example.myminiproject.ui.viewmodels.AuthViewModel
import com.example.myminiproject.utils.NotificationHelper
import com.example.myminiproject.utils.SessionManager
import com.example.myminiproject.utils.ToastHelper
import kotlinx.coroutines.delay

@Composable
fun OTPScreen(
        navController: NavController,
        phone: String,
        verificationId: String,
        name: String,
        mode: String,
        sessionManager: SessionManager,
        authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    var otp by remember { mutableStateOf(List(6) { "" }) }
    var error by remember { mutableStateOf("") }
    var resendTimer by remember { mutableIntStateOf(30) }
    var visible by remember { mutableStateOf(false) }
    val focusRequesters = remember { List(6) { FocusRequester() } }

    val authState by authViewModel.uiState.collectAsState()
    val loading = authState is AuthUiState.Loading

    if (authState is AuthUiState.Error) {
        LaunchedEffect(authState) {
            error = (authState as AuthUiState.Error).message
            ToastHelper.showInvalidOtp(context)
        }
    }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.VerificationSuccess) {
            // Start session on successful authentication
            val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                sessionManager.startSession(userId)
                println("Session started for user: $userId")
                // Show OTP verified notification
                notificationHelper.showOtpVerified()
                ToastHelper.showOtpVerified(context)
            }

            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Splash.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) { visible = true }

    // Countdown timer
    LaunchedEffect(resendTimer) {
        if (resendTimer > 0) {
            delay(1000)
            resendTimer--
        }
    }

    val maskedPhone = if (phone.length >= 4) "${phone.take(2)}XXXXXX${phone.takeLast(2)}" else phone

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        // Header
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.White)
                                .padding(top = 48.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                    onClick = { navController.popBackStack() },
                    modifier =
                            Modifier.clip(RoundedCornerShape(12.dp)).background(Gray100).size(40.dp)
            ) {
                Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Gray600,
                        modifier = Modifier.size(20.dp)
                )
            }
            Box(
                    modifier =
                            Modifier.size(28.dp).clip(RoundedCornerShape(8.dp)).background(Blue600),
                    contentAlignment = Alignment.Center
            ) { Text("DS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White) }
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
                        Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Blue600,
                                modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                            stringResource(R.string.verify_otp),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.enter_otp), fontSize = 15.sp, color = Gray500)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                            "+91 $maskedPhone",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Blue600
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // OTP Inputs
                    Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        otp.forEachIndexed { idx, digit ->
                            OutlinedTextField(
                                    value = digit,
                                    onValueChange = { value ->
                                        if (value.length <= 1 && value.all { it.isDigit() }) {
                                            val newOtp = otp.toMutableList()
                                            newOtp[idx] = value
                                            otp = newOtp
                                            error = ""
                                            if (value.isNotEmpty() && idx < 5) {
                                                focusRequesters[idx + 1].requestFocus()
                                            }
                                        }
                                    },
                                    modifier =
                                            Modifier.width(48.dp)
                                                    .height(56.dp)
                                                    .focusRequester(focusRequesters[idx]),
                                    textStyle =
                                            androidx.compose.ui.text.TextStyle(
                                                    fontSize = 22.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    textAlign = TextAlign.Center,
                                                    color = Blue900
                                            ),
                                    singleLine = true,
                                    keyboardOptions =
                                            KeyboardOptions(keyboardType = KeyboardType.Number),
                                    shape = RoundedCornerShape(12.dp),
                                    colors =
                                            OutlinedTextFieldDefaults.colors(
                                                    focusedBorderColor = Blue600,
                                                    unfocusedBorderColor =
                                                            if (digit.isNotEmpty()) Blue600
                                                            else Gray200,
                                                    focusedContainerColor =
                                                            if (digit.isNotEmpty()) Blue50
                                                            else Color.White,
                                                    unfocusedContainerColor =
                                                            if (digit.isNotEmpty()) Blue50
                                                            else Color.White
                                            )
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    if (error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(error, color = Red500, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resend timer
                    if (resendTimer > 0) {
                        Text(
                                text = "${stringResource(R.string.resend_otp)} in ${resendTimer}s",
                                fontSize = 13.sp,
                                color = Gray400
                        )
                    } else {
                        TextButton(onClick = { resendTimer = 30 }) {
                            Text(
                                    stringResource(R.string.resend_otp),
                                    fontWeight = FontWeight.SemiBold,
                                    color = Blue600
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Verify button
                    val otpCode = otp.joinToString("")
                    Button(
                            onClick = {
                                if (otpCode.length == 6) {
                                    authViewModel.verifyOtp(verificationId, otpCode, name, mode)
                                } else {
                                    ToastHelper.showFieldEmptyError(context, "OTP (all 6 digits)")
                                }
                            },
                            enabled = otpCode.length == 6 && !loading,
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors =
                                    ButtonDefaults.buttonColors(
                                            containerColor =
                                                    if (otpCode.length == 6) Blue600 else Blue400,
                                            disabledContainerColor = Blue400.copy(alpha = 0.5f)
                                    )
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                    stringResource(R.string.loading),
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                    stringResource(R.string.verify),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Demo hint
                    Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Amber50)
                    ) {
                        Text(
                                text = "💡 Demo: Enter any 6 digits to continue",
                                fontSize = 12.sp,
                                color = Amber600,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth().padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
