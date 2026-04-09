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

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.Dashboard.route,
        Screen.TrackMoney.route,
        Screen.Learn.route,
        Screen.Schemes.route,
        Screen.Profile.route
    )
    val showBottomNav = currentRoute in bottomNavRoutes

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
                SplashScreen(navController = navController)
            }

            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }

            composable(Screen.SignUp.route) {
                SignUpScreen(navController = navController)
            }

            composable(
                route = Screen.OTP.route,
                arguments = listOf(
                    navArgument("phone") { type = NavType.StringType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("mode") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val phone = backStackEntry.arguments?.getString("phone") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val mode = backStackEntry.arguments?.getString("mode") ?: "login"
                OTPScreen(
                    navController = navController,
                    phone = phone,
                    name = name,
                    mode = mode
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
                ProfileScreen(navController = navController)
            }

            composable(Screen.ChatBot.route) {
                ChatBotScreen(navController = navController)
            }
        }
    }
}
