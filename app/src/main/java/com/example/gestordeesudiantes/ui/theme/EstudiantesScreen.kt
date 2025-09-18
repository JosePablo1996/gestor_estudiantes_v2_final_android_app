package com.ufg.estudiantes.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudiantesScreen(vm: EstudiantesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Maneja la visualización del Snackbar para los mensajes de éxito
    LaunchedEffect(key1 = state.info) {
        state.info?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                vm.limpiarSeleccion() // Limpia la seleccion despues de una accion exitosa
            }
        }
    }

    // Maneja la visualización del Snackbar para los mensajes de error
    LaunchedEffect(key1 = state.error) {
        state.error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    actionLabel = "OK"
                )
                vm.clearError() // Llama a la nueva funcion del ViewModel para limpiar el error
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Estudiantes", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    // Botón para eliminar todos (NUEVO) - Usando Icons.Default.Delete
                    IconButton(
                        onClick = { vm.setShowDeleteAllDialog(true) },
                        enabled = !state.loading && state.lista.isNotEmpty()
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar todos",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    IconButton(onClick = { vm.refresh() }, enabled = !state.loading) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                if (state.seleccionadoId == null) {
                    FilledTonalButton(
                        onClick = { vm.agregar() },
                        enabled = !state.loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(ButtonDefaults.IconSize))
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Agregar Estudiante")
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = { vm.actualizar() },
                            enabled = !state.loading,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Actualizar", modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Actualizar")
                        }
                        FilledTonalButton(
                            onClick = { vm.limpiarSeleccion() },
                            enabled = !state.loading,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(10.dp)
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(ButtonDefaults.IconSize))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = "Limpiar")
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campos de texto para nombre y edad
            OutlinedTextField(
                value = state.nombre,
                onValueChange = vm::setNombre,
                label = { Text("Nombre") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.edad,
                onValueChange = vm::setEdad,
                label = { Text("Edad") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Edad") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Contenedor para la lista de estudiantes
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.lista) { est ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(est.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Edad: ${est.edad}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(
                                    onClick = { vm.seleccionar(est) },
                                    enabled = !state.loading
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Editar",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        vm.seleccionar(est)
                                        vm.setShowDeleteDialog(true)
                                    },
                                    enabled = !state.loading
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                    item { Spacer(Modifier.height(100.dp)) }
                }
            }
        }
    }

    // Modal de confirmación para eliminar
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { vm.setShowDeleteDialog(false) },
            title = { Text(text = "Confirmar eliminación") },
            text = { Text(text = "¿Estás seguro de que quieres eliminar al estudiante?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val id = state.seleccionadoId
                        if (id != null) {
                            vm.eliminar(id)
                        }
                        vm.setShowDeleteDialog(false)
                        vm.limpiarSeleccion()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        vm.setShowDeleteDialog(false)
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal de confirmación para eliminar todos (NUEVO)
    if (state.showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { vm.setShowDeleteAllDialog(false) },
            title = { Text(text = "⚠️ ELIMINAR TODOS") },
            text = {
                Text(text = "¿Estás seguro de que quieres eliminar TODOS los estudiantes? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.confirmarEliminacionTodos()
                        vm.setShowDeleteAllDialog(false)
                    }
                ) {
                    Text("ELIMINAR TODO", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        vm.setShowDeleteAllDialog(false)
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (state.loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}