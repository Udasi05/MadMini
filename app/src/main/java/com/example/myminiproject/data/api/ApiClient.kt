package com.example.myminiproject.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Network Configuration
    // Set USE_EMULATOR = true for Android Emulator
    // Set USE_EMULATOR = false for Real Device (WiFi debugging)
    private const val USE_EMULATOR = false
    
    // Your computer's IP address for WiFi debugging
    // Find it using: ipconfig (Windows) or ifconfig (Mac/Linux)
    private const val WIFI_IP = "192.168.0.105"
    
    private val BASE_URL = if (USE_EMULATOR) {
        "http://10.0.2.2:8000/"  // For Android Emulator
    } else {
        "http://$WIFI_IP:8000/"  // For Real Device via WiFi
    }

    private val httpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        println("API Configuration:")
        println("Environment: ${if (USE_EMULATOR) "Android Emulator" else "Real Device (WiFi)"}")
        println("Base URL: $BASE_URL")
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
