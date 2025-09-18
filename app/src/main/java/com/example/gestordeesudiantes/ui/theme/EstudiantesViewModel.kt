package com.ufg.estudiantes.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ufg.estudiantes.data.EstudiantesRepository
import com.ufg.estudiantes.data.EstudianteResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class UiState(
    val lista: List<EstudianteResponse> = emptyList(),
    val nombre: String = "",
    val edad: String = "",
    val seleccionadoId: Int? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val info: String? = null,
    val showDeleteDialog: Boolean = false,
    val showToast: Boolean = false,
    val showDeleteAllDialog: Boolean = false // Nuevo estado para el diálogo de eliminar todos
)

class EstudiantesViewModel(
    private val repo: EstudiantesRepository = EstudiantesRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    init { refresh() }

    fun setNombre(value: String) { _uiState.value = _uiState.value.copy(nombre = value) }
    fun setEdad(value: String)   { _uiState.value = _uiState.value.copy(edad = value) }
    fun setShowDeleteDialog(value: Boolean) { _uiState.value = _uiState.value.copy(showDeleteDialog = value) }
    fun setShowDeleteAllDialog(value: Boolean) { _uiState.value = _uiState.value.copy(showDeleteAllDialog = value) }
    fun setShowToast(value: Boolean) { _uiState.value = _uiState.value.copy(showToast = value) }
    fun clearError() { _uiState.value = _uiState.value.copy(error = null) }

    fun seleccionar(est: EstudianteResponse) {
        _uiState.value = _uiState.value.copy(
            seleccionadoId = est.id,
            nombre = est.nombre,
            edad = est.edad.toString(),
            info = "Seleccionado ID ${est.id}"
        )
    }

    fun limpiarSeleccion() {
        _uiState.value = _uiState.value.copy(
            seleccionadoId = null, nombre = "", edad = "", info = "Limpio"
        )
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, info = null)
            try {
                val data = repo.list() // ← Usando el método existente 'list()'
                _uiState.value = _uiState.value.copy(lista = data, loading = false)
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Error de autenticación: API Key inválida o faltante"
                    e.message?.contains("HTTP 401") == true -> "Error de autenticación: API Key inválida o faltante"
                    e.message?.contains("Unable to resolve host") == true -> "Error de conexión: Verifica tu internet"
                    else -> "Error al conectar con la API: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(loading = false, error = errorMessage)
            }
        }
    }

    fun agregar() {
        val nombre = _uiState.value.nombre.trim()
        val edad = _uiState.value.edad.toIntOrNull()
        if (nombre.isEmpty() || edad == null) {
            _uiState.value = _uiState.value.copy(error = "Nombre y edad válidos requeridos")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, info = null)
            try {
                repo.add(nombre, edad) // ← Usando el método existente 'add()'
                refresh()
                _uiState.value = _uiState.value.copy(
                    info = "Estudiante agregado correctamente",
                    nombre = "",
                    edad = "",
                    showToast = true,
                    loading = false
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Error de autenticación al agregar: API Key inválida"
                    e.message?.contains("HTTP 401") == true -> "Error de autenticación: API Key inválida o faltante"
                    else -> "Error al agregar estudiante: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(loading = false, error = errorMessage)
            }
        }
    }

    fun actualizar() {
        val id = _uiState.value.seleccionadoId ?: run {
            _uiState.value = _uiState.value.copy(error = "Selecciona un estudiante primero")
            return
        }
        val nombre = _uiState.value.nombre.trim()
        val edad = _uiState.value.edad.toIntOrNull()
        if (nombre.isEmpty() || edad == null) {
            _uiState.value = _uiState.value.copy(error = "Nombre y edad válidos requeridos")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, info = null)
            try {
                repo.update(id, nombre, edad) // ← Usando el método existente 'update()'
                refresh()
                _uiState.value = _uiState.value.copy(
                    info = "Estudiante actualizado correctamente",
                    loading = false
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Error de autenticación al actualizar: API Key inválida"
                    else -> "Error al actualizar: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(loading = false, error = errorMessage)
            }
        }
    }

    fun eliminar(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, info = null)
            try {
                repo.delete(id) // ← Usando el método existente 'delete()'
                refresh()
                _uiState.value = _uiState.value.copy(
                    info = "Estudiante eliminado correctamente",
                    loading = false
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Error de autenticación al eliminar: API Key inválida"
                    else -> "Error al eliminar: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(loading = false, error = errorMessage)
            }
        }
    }

    fun confirmarEliminacion() {
        val id = _uiState.value.seleccionadoId ?: return
        eliminar(id)
        setShowDeleteDialog(false)
    }

    // Nuevo método para eliminar todos los estudiantes
    fun eliminarTodos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, info = null)
            try {
                val resultado = repo.deleteAllEstudiantes() // ← Usando el nuevo método
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    info = "✅ ${resultado.eliminados} estudiantes eliminados",
                    lista = emptyList(), // Limpiar la lista local
                    showToast = true,
                    showDeleteAllDialog = false // Cerrar el diálogo
                )
            } catch (e: Exception) {
                val errorMessage = when {
                    e.message?.contains("401") == true -> "Error de autenticación: API Key inválida"
                    e.message?.contains("confirmación") == true -> "Se requiere confirmación explícita"
                    else -> "❌ Error al eliminar todos: ${e.message}"
                }
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    error = errorMessage,
                    showToast = true,
                    showDeleteAllDialog = false // Cerrar el diálogo incluso en error
                )
            }
        }
    }

    fun confirmarEliminacionTodos() {
        eliminarTodos()
        setShowDeleteAllDialog(false)
    }
}