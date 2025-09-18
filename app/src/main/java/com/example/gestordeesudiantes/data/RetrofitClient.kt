package com.ufg.estudiantes.data

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://dispositivos-moviles-fastapi.onrender.com/"
    private const val API_KEY = "gestor_estudiantes_key_2025" // ← KEY CORREGIDA
    private const val TAG = "RetrofitDebug"

    // Interceptor de autenticación MEJORADO
    private val authInterceptor = okhttp3.Interceptor { chain ->
        val originalRequest = chain.request()

        // Debug: verificar la request original
        Log.d(TAG, "Original request: ${originalRequest.url}")
        Log.d(TAG, "Original headers: ${originalRequest.headers}")

        // Construir nueva request con API Key
        val newRequest = originalRequest.newBuilder()
            .header("X-API-Key", API_KEY)
            .apply {
                // Asegurar content-type para requests POST/PUT
                if (originalRequest.body != null && originalRequest.header("Content-Type") == null) {
                    header("Content-Type", "application/json")
                }
            }
            .build()

        // Debug: verificar la nueva request
        Log.d(TAG, "New request with API Key: $API_KEY")
        Log.d(TAG, "New headers: ${newRequest.headers}")

        chain.proceed(newRequest)
    }

    // Interceptor de logging MEJORADO
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor) // ← PRIMERO la autenticación
        .addInterceptor(loggingInterceptor) // ← LUEGO el logging
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}