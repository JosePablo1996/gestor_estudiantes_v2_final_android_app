package com.ufg.estudiantes.data

// Lo que tu API devuelve al listar
data class EstudianteResponse(
    val id: Int,
    val nombre: String,
    val edad: Int
)

// Lo que envías al crear/actualizar
data class EstudiantePayload(
    val nombre: String,
    val edad: Int
)

// Envoltorio que usa tu API para POST/PUT/DELETE
// Ejemplos:
//  POST -> { "mensaje": "...", "data": 3 }
//  PUT  -> { "mensaje": "...", "data": {id, nombre, edad} }
//  DEL  -> { "mensaje": "..." }
data class ApiResult<T>(
    val mensaje: String,
    val data: T?
)
