package com.example.myminiproject.utils

import android.content.Context
import android.widget.Toast

object ToastHelper {
    /**
     * Show a short toast notification
     * @param context The application context
     * @param message The message to display
     */
    fun showShort(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Show a long toast notification
     * @param context The application context
     * @param message The message to display
     */
    fun showLong(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Success Messages
    fun showLoginSuccess(context: Context) {
        showShort(context, "✓ Login successful!")
    }

    fun showLogoutSuccess(context: Context) {
        showShort(context, "✓ Logged out successfully")
    }

    fun showSignupSuccess(context: Context) {
        showShort(context, "✓ Account created successfully!")
    }

    fun showOtpSent(context: Context, phone: String) {
        showShort(context, "✓ OTP sent to $phone")
    }

    fun showOtpVerified(context: Context) {
        showShort(context, "✓ OTP verified successfully!")
    }

    fun showTransactionAdded(context: Context, amount: String) {
        showShort(context, "✓ Transaction added: ₹$amount")
    }

    fun showProfileUpdated(context: Context) {
        showShort(context, "✓ Profile updated successfully")
    }

    fun showDataExported(context: Context) {
        showShort(context, "✓ Data exported successfully")
    }

    fun showNotificationToggled(context: Context, isEnabled: Boolean) {
        val message = if (isEnabled) "✓ Notifications enabled" else "✓ Notifications disabled"
        showShort(context, message)
    }

    fun showTicketCreated(context: Context) {
        showShort(context, "✓ Support ticket created")
    }

    // Error Messages
    fun showError(context: Context, message: String) {
        showShort(context, "✗ $message")
    }

    fun showInvalidPhone(context: Context) {
        showShort(context, "✗ Please enter a valid phone number")
    }

    fun showInvalidOtp(context: Context) {
        showShort(context, "✗ Invalid OTP. Please try again")
    }

    fun showLoginError(context: Context, error: String = "Login failed") {
        showShort(context, "✗ $error")
    }

    fun showNetworkError(context: Context) {
        showShort(context, "✗ Network error. Please check your connection")
    }

    fun showSessionExpired(context: Context) {
        showLong(context, "✗ Your session has expired. Please login again")
    }

    fun showFieldEmptyError(context: Context, fieldName: String) {
        showShort(context, "✗ Please enter $fieldName")
    }

    fun showTransactionError(context: Context, error: String = "Failed to add transaction") {
        showShort(context, "✗ $error")
    }

    // Info Messages
    fun showInfo(context: Context, message: String) {
        showShort(context, "ℹ $message")
    }

    fun showWelcome(context: Context, name: String) {
        showShort(context, "Welcome, $name!")
    }

    fun showLoading(context: Context, message: String = "Loading...") {
        showShort(context, message)
    }
}
