package com.ufg.estudiantes.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // URL actualizada para usar la API desplegada en Render
    private const val BASE_URL = "https://dispositivos-moviles-fastapi.onrender.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Configuración de timeouts extendidos para Render (puede tardar en "despertar")
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)    // Timeout de conexión extendido
        .readTimeout(60, TimeUnit.SECONDS)       // Timeout de lectura extendido
        .writeTimeout(60, TimeUnit.SECONDS)      // Timeout de escritura extendido
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