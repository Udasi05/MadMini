package com.example.myminiproject.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun LoginScreen(
        navController: NavController,
        sessionManager: SessionManager,
        authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val notificationHelper = remember { NotificationHelper(context) }
    var phone by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(false) }

    val authState by authViewModel.uiState.collectAsState()
    val loading = authState is AuthUiState.Loading

    // Initialize Google Sign-In Client
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(
                        "677560045275-ddvf50elrfsni2rrti1eq8fef0p6m7um.apps.googleusercontent.com"
                )
                .requestEmail()
                .build()
    }
    val googleSignInClient: GoogleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Google Sign-In Launcher
    val googleSignInLauncher =
            rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    result ->
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        authViewModel.signInWithGoogle(account)
                    }
                } catch (e: ApiException) {
                    ToastHelper.showLoginError(context, "Google Sign-In failed: ${e.message}")
                }
            }

    LaunchedEffect(Unit) { visible = true }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthUiState.CodeSent -> {
                val verificationId = (authState as AuthUiState.CodeSent).verificationId
                ToastHelper.showOtpSent(context, phone)
                navController.navigate(
                        Screen.OTP.createRoute(phone, verificationId, "User", "login")
                )
            }
            is AuthUiState.VerificationSuccess -> {
                // Start session on successful authentication
                val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                val userName =
                        com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.displayName
                if (userId != null) {
                    sessionManager.startSession(userId)
                    println("Session started for user: $userId")
                    // Show login success notification
                    notificationHelper.showLoginSuccess(userName)
                    ToastHelper.showLoginSuccess(context)
                }

                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
            is AuthUiState.Error -> {
                val errorMessage = (authState as AuthUiState.Error).message
                ToastHelper.showLoginError(context, errorMessage)
            }
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Gray50)) {
        // Header
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.White)
                                .padding(top = 32.dp, bottom = 12.dp, start = 16.dp, end = 16.dp),
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
                        contentDescription = stringResource(R.string.back),
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
                    // Icon
                    Box(
                            modifier = Modifier.size(64.dp).clip(CircleShape).background(Blue50),
                            contentAlignment = Alignment.Center
                    ) {
                        Icon(
                                Icons.Default.Phone,
                                contentDescription = null,
                                tint = Blue600,
                                modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                            text = stringResource(R.string.welcome_back),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate800
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                            text = stringResource(R.string.login_subtitle),
                            fontSize = 14.sp,
                            color = Gray500,
                            textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Phone Input
                    Text(
                            text = stringResource(R.string.phone_number),
                            fontSize = 14.sp,
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
                                modifier =
                                        Modifier.clip(RoundedCornerShape(12.dp))
                                                .background(Color.White)
                                                .border(1.dp, Gray200, RoundedCornerShape(12.dp))
                                                .padding(horizontal = 12.dp, vertical = 16.dp)
                        ) {
                            Text(
                                    "+91",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Gray600
                            )
                        }
                        OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
                                placeholder = {
                                    Text("Enter 10-digit mobile number", fontSize = 15.sp)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true,
                                keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Phone),
                                colors =
                                        OutlinedTextFieldDefaults.colors(
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
                                    // Real Auth implementation
                                    val activity = context as? android.app.Activity
                                    if (activity != null) {
                                        authViewModel.sendOtp(phone, activity)
                                    }
                                } else {
                                    ToastHelper.showInvalidPhone(context)
                                }
                            },
                            enabled = phone.length == 10 && !loading,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Blue600)
                    ) {
                        if (loading) {
                            CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                            )
                        } else {
                            Text("Send OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // OR Divider
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
                        Text(
                                "OR",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                fontSize = 12.sp,
                                color = Gray400,
                                fontWeight = FontWeight.Medium
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Google Login Button
                    OutlinedButton(
                            onClick = {
                                val signInIntent = googleSignInClient.signInIntent
                                googleSignInLauncher.launch(signInIntent)
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Gray200),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Gray700)
                    ) {
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                        ) {
                            // In a real app, you'd use a Google icon resource
                            Box(
                                    modifier =
                                            Modifier.size(18.dp)
                                                    .clip(CircleShape)
                                                    .background(Color.Red),
                                    contentAlignment = Alignment.Center
                            ) {
                                Text(
                                        "G",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                    "Continue with Google",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Footer
        Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                        Icons.Default.Shield,
                        contentDescription = null,
                        tint = Gray400,
                        modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                        "Your data is secured with bank-grade encryption",
                        fontSize = 11.sp,
                        color = Gray400
                )
            }
        }
    }
}
