package com.example.myminiproject.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myminiproject.ui.components.BottomNavBar
import com.example.myminiproject.ui.screens.*
import com.example.myminiproject.utils.SessionManager
//import kotlinx.coroutines.flow.collectAsState

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    sessionManager: SessionManager
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isSessionValid by sessionManager.isSessionValid.collectAsState()

    val bottomNavRoutes = listOf(
        Screen.Dashboard.route,
        Screen.TrackMoney.route,
        Screen.Learn.route,
        Screen.Schemes.route,
        Screen.Profile.route
    )
    val showBottomNav = currentRoute in bottomNavRoutes

    // Check session validity and redirect to login if expired
    LaunchedEffect(isSessionValid, currentRoute) {
        if (!isSessionValid && currentRoute in bottomNavRoutes) {
            println("Session expired, redirecting to login")
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 100 }) },
            exitTransition = { fadeOut(animationSpec = tween(300)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -100 }) },
            popExitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController = navController, sessionManager = sessionManager)
            }

            composable(Screen.Login.route) {
                LoginScreen(navController = navController, sessionManager = sessionManager)
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(navController = navController)
            }

            composable(
                route = Screen.OTP.route,
                arguments = listOf(
                    navArgument("phone") { type = NavType.StringType },
                    navArgument("verificationId") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("mode") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val mode = backStackEntry.arguments?.getString("mode") ?: "login"
                OTPScreen(
                    navController = navController,
                    phone = phone,
                    verificationId = verificationId,
                    name = name,
                    mode = mode,
                    sessionManager = sessionManager
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController)
            }

            composable(Screen.TrackMoney.route) {
                TrackMoneyScreen(navController = navController)
            }

            composable(Screen.Learn.route) {
                LearnScreen(navController = navController)
            }

            composable(Screen.Schemes.route) {
                SchemesScreen(navController = navController)
            }

            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController, sessionManager = sessionManager)
            }

            composable(Screen.EditProfile.route) {
                EditProfileScreen(navController = navController)
            }

            composable(Screen.VoiceInput.route) {
                VoiceInputScreen(navController = navController)
            }

            composable(Screen.ChatBot.route) {
                ChatBotScreen(navController = navController)
            }

            composable(Screen.HelpSupport.route) {
                HelpSupportScreen(navController = navController)
            }

            composable(Screen.MyTickets.route) {
                MyTicketsScreen(navController = navController)
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(navController = navController)
            }

            composable(Screen.DataExport.route) {
                DataExportScreen(navController = navController)
            }

            composable(Screen.NotificationPreferences.route) {
                NotificationPreferencesScreen(navController = navController)
            }
        }
    }
}


