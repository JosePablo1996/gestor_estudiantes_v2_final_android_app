package com.ufg.estudiantes.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Eliminamos la declaración duplicada de EstudianteResponse
// y usamos la que ya existe en tu proyecto

data class EstudianteRequest(
    val nombre: String,
    val edad: Int
)

// Interface para el servicio API
interface EstudiantesApiService {
    @GET("estudiantes")
    suspend fun getEstudiantes(): List<EstudianteResponse>

    @POST("estudiantes")
    suspend fun createEstudiante(@Body estudiante: EstudianteRequest): EstudianteResponse

    @PUT("estudiantes/{id}")
    suspend fun updateEstudiante(@Path("id") id: Int, @Body estudiante: EstudianteRequest): EstudianteResponse

    @DELETE("estudiantes/{id}")
    suspend fun deleteEstudiante(@Path("id") id: Int)
}

class EstudiantesRepository {
    private val apiService: EstudiantesApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dispositivos-moviles-fastapi.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(EstudiantesApiService::class.java)
    }

    suspend fun list(): List<EstudianteResponse> {
        return apiService.getEstudiantes()
    }

    suspend fun add(nombre: String, edad: Int) {
        apiService.createEstudiante(EstudianteRequest(nombre, edad))
    }

    suspend fun update(id: Int, nombre: String, edad: Int) {
        apiService.updateEstudiante(id, EstudianteRequest(nombre, edad))
    }

    suspend fun delete(id: Int) {
        apiService.deleteEstudiante(id)
    }
}