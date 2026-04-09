package com.example.myminiproject.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object OTP : Screen("otp/{phone}/{name}/{mode}") {
        fun createRoute(phone: String, name: String, mode: String): String =
            "otp/$phone/$name/$mode"
    }
    object Dashboard : Screen("dashboard")
    object TrackMoney : Screen("track")
    object Learn : Screen("learn")
    object Schemes : Screen("schemes")
    object Profile : Screen("profile")
    object ChatBot : Screen("chatbot")
}
