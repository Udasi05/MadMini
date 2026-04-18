package com.example.myminiproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myminiproject.data.api.ApiClient
import com.example.myminiproject.data.api.dto.ChatRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

data class ChatMessage(
    val id: String,
    val text: String,
    val sender: String, // "user" or "bot"
    val timestamp: Date = Date()
)

class ChatBotViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                id = System.currentTimeMillis().toString(),
                text = "Hello! I'm your DhanSathi assistant. How can I help you with your finances today? 👋",
                sender = "bot"
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    fun sendMessage(text: String, language: String = "english", context: String? = "general") {
        if (text.isBlank()) return

        // 1. Add user message
        val userMsg = ChatMessage(
            id = System.currentTimeMillis().toString(),
            text = text,
            sender = "user"
        )
        _messages.value = _messages.value + userMsg
        _isTyping.value = true

        // 2. Fetch bot response
        viewModelScope.launch {
            try {
                val request = ChatRequest(
                    message = text,
                    language = language,
                    context = context
                )
                val response = ApiClient.apiService.chatWithBot(request)

                if (response.isSuccessful && response.body() != null) {
                    val botText = response.body()!!.botResponse.english // or marathi based on selected language
                    
                    val botMsg = ChatMessage(
                        id = (System.currentTimeMillis() + 1).toString(),
                        text = botText,
                        sender = "bot"
                    )
                    _messages.value = _messages.value + botMsg
                } else {
                    handleError("Could not reach the assistant. Please try again.")
                }
            } catch (e: Exception) {
                handleError("Connection error: ${e.message}")
            } finally {
                _isTyping.value = false
            }
        }
    }

    private fun handleError(msg: String) {
        _messages.value = _messages.value + ChatMessage(
            id = (System.currentTimeMillis() + 1).toString(),
            text = msg,
            sender = "bot"
        )
    }
}
