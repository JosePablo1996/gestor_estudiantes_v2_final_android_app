package com.ufg.estudiantes.data

import android.util.Log
import retrofit2.HttpException
import java.io.IOException

class EstudiantesRepository {
    private val apiService: ApiService = RetrofitClient.api
    private val TAG = "EstudiantesRepository"

    suspend fun list(): List<EstudianteResponse> {
        try {
            Log.d(TAG, "Obteniendo lista de estudiantes...")
            val response = apiService.getEstudiantes()

            if (response.isSuccessful) {
                val estudiantes = response.body() ?: emptyList()
                Log.d(TAG, "✅ Lista obtenida exitosamente: ${estudiantes.size} estudiantes")
                return estudiantes
            } else {
                Log.e(TAG, "❌ Error HTTP al obtener lista: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    401 -> throw Exception("Error de autenticación: API Key inválida o faltante")
                    404 -> throw Exception("Endpoint no encontrado")
                    500 -> throw Exception("Error interno del servidor")
                    else -> throw Exception("Error del servidor: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "❌ Error HTTP al obtener lista: ${e.code()} - ${e.message()}")
            when (e.code()) {
                401 -> throw Exception("Error de autenticación: API Key inválida o faltante")
                404 -> throw Exception("Endpoint no encontrado")
                500 -> throw Exception("Error interno del servidor")
                else -> throw Exception("Error del servidor: ${e.code()} - ${e.message()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "❌ Error de conexión: ${e.message}")
            throw Exception("Error de conexión: Verifica tu internet")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inesperado: ${e.message}")
            throw Exception("Error inesperado: ${e.message}")
        }
    }

    suspend fun add(nombre: String, edad: Int) {
        try {
            Log.d(TAG, "Agregando estudiante: $nombre, $edad años")
            val response = apiService.addEstudiante(EstudiantePayload(nombre, edad))

            if (response.isSuccessful) {
                val estudianteCreado = response.body()
                Log.d(TAG, "✅ Estudiante agregado exitosamente: ID ${estudianteCreado?.id}")
            } else {
                Log.e(TAG, "❌ Error HTTP al agregar: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    401 -> throw Exception("Error de autenticación al agregar: API Key inválida")
                    400 -> throw Exception("Datos inválidos: verifica nombre y edad")
                    500 -> throw Exception("Error interno del servidor al agregar")
                    else -> throw Exception("Error del servidor: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "❌ Error HTTP al agregar: ${e.code()} - ${e.message()}")
            when (e.code()) {
                401 -> throw Exception("Error de autenticación al agregar: API Key inválida")
                400 -> throw Exception("Datos inválidos: verifica nombre y edad")
                500 -> throw Exception("Error interno del servidor al agregar")
                else -> throw Exception("Error del servidor: ${e.code()} - ${e.message()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "❌ Error de conexión al agregar: ${e.message}")
            throw Exception("Error de conexión al agregar estudiante")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inesperado al agregar: ${e.message}")
            throw Exception("Error al agregar estudiante: ${e.message}")
        }
    }

    suspend fun update(id: Int, nombre: String, edad: Int) {
        try {
            Log.d(TAG, "Actualizando estudiante ID $id: $nombre, $edad años")
            val response = apiService.updateEstudiante(id, EstudiantePayload(nombre, edad))

            if (response.isSuccessful) {
                val estudianteActualizado = response.body()
                Log.d(TAG, "✅ Estudiante actualizado exitosamente: ID ${estudianteActualizado?.id}")
            } else {
                Log.e(TAG, "❌ Error HTTP al actualizar: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    401 -> throw Exception("Error de autenticación al actualizar: API Key inválida")
                    404 -> throw Exception("Estudiante no encontrado (ID: $id)")
                    400 -> throw Exception("Datos inválidos para actualizar")
                    500 -> throw Exception("Error interno del servidor al actualizar")
                    else -> throw Exception("Error del servidor: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "❌ Error HTTP al actualizar: ${e.code()} - ${e.message()}")
            when (e.code()) {
                401 -> throw Exception("Error de autenticación al actualizar: API Key inválida")
                404 -> throw Exception("Estudiante no encontrado (ID: $id)")
                400 -> throw Exception("Datos inválidos para actualizar")
                500 -> throw Exception("Error interno del servidor al actualizar")
                else -> throw Exception("Error del servidor: ${e.code()} - ${e.message()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "❌ Error de conexión al actualizar: ${e.message}")
            throw Exception("Error de conexión al actualizar estudiante")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inesperado al actualizar: ${e.message}")
            throw Exception("Error al actualizar estudiante: ${e.message}")
        }
    }

    suspend fun delete(id: Int) {
        try {
            Log.d(TAG, "Eliminando estudiante ID: $id")
            val response = apiService.deleteEstudiante(id)

            if (response.isSuccessful) {
                Log.d(TAG, "✅ Estudiante eliminado exitosamente: ID $id")
            } else {
                Log.e(TAG, "❌ Error HTTP al eliminar: ${response.code()} - ${response.message()}")
                when (response.code()) {
                    401 -> throw Exception("Error de autenticación al eliminar: API Key inválida")
                    404 -> throw Exception("Estudiante no encontrado (ID: $id)")
                    500 -> throw Exception("Error interno del servidor al eliminar")
                    else -> throw Exception("Error del servidor: ${response.code()} - ${response.message()}")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "❌ Error HTTP al eliminar: ${e.code()} - ${e.message()}")
            when (e.code()) {
                401 -> throw Exception("Error de autenticación al eliminar: API Key inválida")
                404 -> throw Exception("Estudiante no encontrado (ID: $id)")
                500 -> throw Exception("Error interno del servidor al eliminar")
                else -> throw Exception("Error del servidor: ${e.code()} - ${e.message()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "❌ Error de conexión al eliminar: ${e.message}")
            throw Exception("Error de conexión al eliminar estudiante")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error inesperado al eliminar: ${e.message}")
            throw Exception("Error al eliminar estudiante: ${e.message}")
        }
    }

    // Método para eliminar todos los estudiantes (versión simplificada)
    suspend fun deleteAllEstudiantes(): DeleteAllResponse {
        try {
            Log.d(TAG, "🗑️ Intentando eliminar TODOS los estudiantes")
            val response = apiService.deleteAllEstudiantes(confirmacion = true)

            if (response.isSuccessful) {
                val resultado = response.body()
                Log.d(TAG, "✅ Todos los estudiantes eliminados: ${resultado?.eliminados} registros")
                return resultado ?: throw Exception("Respuesta vacía del servidor")
            } else {
                Log.e(TAG, "❌ Error al eliminar todos: ${response.code()}")
                when (response.code()) {
                    401 -> throw Exception("Error de autenticación: API Key inválida")
                    400 -> throw Exception("Se requiere confirmación explícita")
                    500 -> throw Exception("Error interno del servidor")
                    else -> throw Exception("Error del servidor: ${response.code()}")
                }
            }
        } catch (e: HttpException) {
            Log.e(TAG, "❌ Error HTTP al eliminar todos: ${e.code()} - ${e.message()}")
            when (e.code()) {
                401 -> throw Exception("Error de autenticación: API Key inválida")
                400 -> throw Exception("Se requiere confirmación explícita")
                500 -> throw Exception("Error interno del servidor")
                else -> throw Exception("Error del servidor: ${e.code()}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "❌ Error de conexión al eliminar todos: ${e.message}")
            throw Exception("Error de conexión: Verifica tu internet")
        } catch (e: Exception) {
            Log.e(TAG, "💥 Error inesperado al eliminar todos: ${e.message}")
            throw Exception("Error al eliminar todos los estudiantes: ${e.message}")
        }
    }
}