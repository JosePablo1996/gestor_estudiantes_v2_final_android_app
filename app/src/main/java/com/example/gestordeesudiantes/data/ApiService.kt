package com.ufg.estudiantes.data

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("estudiantes/")
    suspend fun getEstudiantes(): Response<List<EstudianteResponse>>

    @GET("estudiantes/{id}")
    suspend fun getEstudianteById(@Path("id") id: Int): Response<EstudianteResponse>

    @POST("estudiantes/")
    suspend fun addEstudiante(@Body payload: EstudiantePayload): Response<EstudianteResponse>

    @PUT("estudiantes/{id}")
    suspend fun updateEstudiante(
        @Path("id") id: Int,
        @Body payload: EstudiantePayload
    ): Response<EstudianteResponse>

    @DELETE("estudiantes/{id}")
    suspend fun deleteEstudiante(@Path("id") id: Int): Response<Unit>

    @DELETE("estudiantes/admin/delete-all")
    suspend fun deleteAllEstudiantes(
        @Query("confirmacion") confirmacion: Boolean = true
    ): Response<DeleteAllResponse>
}

// Agrega esta clase de respuesta
data class DeleteAllResponse(
    val mensaje: String,
    val eliminados: Int,
    val restantes: Int
)