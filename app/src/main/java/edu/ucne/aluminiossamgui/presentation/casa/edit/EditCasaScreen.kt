package edu.ucne.aluminiossamgui.presentation.casa.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditCasaScreen(
    casaId: Int?,
    viewModel: EditCasaViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(casaId) {
        viewModel.onEvent(EditCasaUiEvent.Load(casaId))
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    EditCasaBody(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCasaBody(
    state: EditCasaUiState,
    onEvent: (EditCasaUiEvent) -> Unit,
    onBack: () -> Unit
) {
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                onEvent(EditCasaUiEvent.DismissDeleteDialog)
            },
            title = { Text("Eliminar casa") },
            text = {
                Text("¿Deseas eliminar esta casa? También se eliminarán todos sus huecos.")
            },
            confirmButton = {
                TextButton(onClick = { onEvent(EditCasaUiEvent.Delete) }) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(EditCasaUiEvent.DismissDeleteDialog) }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (state.isNew) { "Nueva Casa" } else { "Editar Casa" }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = state.nombre,
                        onValueChange = { onEvent(EditCasaUiEvent.NombreChanged(it)) },
                        label = { Text("Nombre de la casa") },
                        isError = state.nombreError != null,
                        supportingText = { state.nombreError?.let { Text(it) } },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.direccion,
                        onValueChange = { onEvent(EditCasaUiEvent.DireccionChanged(it)) },
                        label = { Text("Dirección (opcional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    state.errorMessage?.let { message ->
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = { onEvent(EditCasaUiEvent.Save) },
                            enabled = !state.isSaving && !state.isDeleting
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null)
                            Text(if (state.isSaving) { "Guardando..." } else { "Guardar" })
                        }

                        if (!state.isNew) {
                            OutlinedButton(
                                onClick = { onEvent(EditCasaUiEvent.ShowDeleteDialog) },
                                enabled = !state.isSaving && !state.isDeleting,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)

                                Text(if (state.isDeleting) { "Eliminando..." } else { "Eliminar" })
                            }
                        }
                    }
                }
            }
        }
    }
}