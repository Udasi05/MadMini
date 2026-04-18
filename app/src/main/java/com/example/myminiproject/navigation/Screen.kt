package com.example.myminiproject.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object OTP : Screen("otp/{phone}/{verificationId}/{name}/{mode}") {
        fun createRoute(phone: String, verificationId: String, name: String, mode: String): String =
            "otp/$phone/$verificationId/$name/$mode"
    }
    object Dashboard : Screen("dashboard")
    object TrackMoney : Screen("track")
    object Learn : Screen("learn")
    object Schemes : Screen("schemes")
    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")
    object VoiceInput : Screen("voice_input")
    object ChatBot : Screen("chatbot")
    object HelpSupport : Screen("help_support")
    object MyTickets : Screen("my_tickets")
    object Analytics : Screen("analytics")
    object DataExport : Screen("data_export")
    object NotificationPreferences : Screen("notification_preferences")
}
