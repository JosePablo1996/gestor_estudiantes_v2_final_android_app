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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstudiantesScreen(vm: EstudiantesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de Estudiantes", style = MaterialTheme.typography.titleLarge) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Formulario
            Text(
                text = if (state.seleccionadoId == null) "Nuevo estudiante" else "Editar ID ${state.seleccionadoId}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = state.nombre,
                onValueChange = vm::setNombre,
                label = { Text("Nombre") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Icono de persona") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = state.edad,
                onValueChange = vm::setEdad,
                label = { Text("Edad") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = "Icono de edad") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = vm::agregar,
                    enabled = !state.loading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                    Spacer(Modifier.width(4.dp))
                    Text("Agregar")
                }
                Button(
                    onClick = vm::actualizar,
                    enabled = state.seleccionadoId != null && !state.loading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Actualizar")
                    Spacer(Modifier.width(4.dp))
                    Text("Actualizar")
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = vm::limpiarSeleccion,
                    enabled = !state.loading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Clear, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Limpiar")
                }
                OutlinedButton(
                    onClick = vm::refresh,
                    enabled = !state.loading,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Refrescar")
                }
            }

            if (state.loading) {
                LinearProgressIndicator(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }

            state.error?.let {
                Spacer(Modifier.height(12.dp))
                AssistChip(
                    onClick = { /* No hay limpiarMensajes en el ViewModel original */ },
                    label = { Text("Error: $it") }
                )
            }
            state.info?.let {
                Spacer(Modifier.height(12.dp))
                AssistChip(
                    onClick = { /* No hay limpiarMensajes en el ViewModel original */ },
                    label = { Text(it) }
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Listado de Estudiantes", style = MaterialTheme.typography.titleMedium)

            // Contador de estudiantes
            Text(
                text = if (state.lista.isEmpty()) "No hay estudiantes registrados"
                else "${state.lista.size} estudiantes encontrados",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.lista, key = { it.id }) { est ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("${est.nombre}", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(4.dp))
                                Text("Edad: ${est.edad}", style = MaterialTheme.typography.bodyMedium)
                                Text("ID: ${est.id}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
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
                                    onClick = { vm.eliminar(est.id) },
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
                }
                item { Spacer(Modifier.height(100.dp)) }
            }
        }
    }
}