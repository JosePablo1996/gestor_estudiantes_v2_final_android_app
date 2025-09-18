package com.ufg.estudiantes.data

import retrofit2.Response
import java.io.IOException

object ApiCallHandler {
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía del servidor - Código: ${response.code()}"))
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "No autorizado - API Key inválida"
                    404 -> "Recurso no encontrado"
                    500 -> "Error interno del servidor"
                    else -> "Error: ${response.code()} - ${response.errorBody()?.string()}"
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: IOException) {
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }
}