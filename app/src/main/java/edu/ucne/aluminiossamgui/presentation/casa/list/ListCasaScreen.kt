package edu.ucne.aluminiossamgui.presentation.casa.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.aluminiossamgui.domain.model.Casa

@Composable
fun ListCasaScreen(
    viewModel: ListCasaViewModel = hiltViewModel(),
    createCasa: () -> Unit,
    goToCasa: (Int) -> Unit,
    goToHuecos: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ListCasaBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is ListCasaUiEvent.CreateNew -> createCasa()
                is ListCasaUiEvent.Edit -> goToCasa(event.id)
                is ListCasaUiEvent.OpenHuecos -> goToHuecos(event.casaId)
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCasaBody(
    state: ListCasaUiState,
    onEvent: (ListCasaUiEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mis Casas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ListCasaUiEvent.CreateNew) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Crear casa")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 80.dp
                )
        ) {
            OutlinedTextField(
                value = state.filtroNombre,
                onValueChange = { onEvent(ListCasaUiEvent.FiltroNombreChanged(it)) },
                label = { Text("Filtrar por nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (state.filtradas.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay casas registradas.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.filtradas,
                        key = { casa -> casa.casaId }
                    ) { casa ->
                        CasaItem(
                            casa = casa,
                            onOpenHuecos = { onEvent(ListCasaUiEvent.OpenHuecos(casa.casaId)) },
                            onEdit = { onEvent(ListCasaUiEvent.Edit(casa.casaId)) }
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Total de casas: ${state.totalCasas}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun CasaItem(
    casa: Casa,
    onOpenHuecos: () -> Unit,
    onEdit: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenHuecos() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = casa.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                if (!casa.direccion.isNullOrBlank()) {
                    Text(
                        text = casa.direccion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Presiona para ver sus huecos",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar casa"
                )
            }
        }
    }
}