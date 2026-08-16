package edu.ucne.aluminiossamgui.presentation.hueco.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco

@Composable
fun ListHuecoScreen(
    casaId: Int,
    viewModel: ListHuecoViewModel = hiltViewModel(),
    onBack: () -> Unit,
    createHueco: (Int) -> Unit,
    goToHueco: (Int, Int) -> Unit,
    editCasa: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(casaId) {
        viewModel.onEvent(ListHuecoUiEvent.Load(casaId))
    }

    ListHuecoBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is ListHuecoUiEvent.CreateNew -> { createHueco(casaId) }
                is ListHuecoUiEvent.Edit -> { goToHueco(casaId, event.id) }
                is ListHuecoUiEvent.EditCasa -> { editCasa(casaId) }
                else -> { viewModel.onEvent(event) }
            }
        },
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListHuecoBody(
    state: ListHuecoUiState,
    onEvent: (ListHuecoUiEvent) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(state.nombreCasa) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(ListHuecoUiEvent.EditCasa) }
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar casa")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ListHuecoUiEvent.CreateNew) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Agregar hueco")
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
                value = state.filtro,
                onValueChange = { onEvent(ListHuecoUiEvent.FiltroChanged(it)) },
                label = { Text("Filtrar por etiqueta o tipo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (state.filtrados.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Esta casa no tiene huecos registrados.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = state.filtrados,
                        key = { hueco -> hueco.huecoId }
                    ) { hueco ->
                        HuecoItem(
                            hueco = hueco,
                            onClick = { onEvent(ListHuecoUiEvent.Edit(hueco.huecoId)) }
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Total de huecos: ${state.totalHuecos}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun HuecoItem(
    hueco: Hueco,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val tipoLabel = when (hueco.tipo) {
                TipoHueco.CORREDERA -> "Corredera"
                TipoHueco.PUERTA -> "Puerta"
                TipoHueco.CRISTAL_FIJO -> "Cristal fijo"
            }

            val unidad = if (hueco.tipo == TipoHueco.PUERTA) {
                "cm"
            } else {
                "pulg."
            }

            val colorLabel = if (
                hueco.colorPersonalizado.isNullOrBlank()
            ) {
                hueco.color.name
            } else {
                hueco.colorPersonalizado
            }

            Text(
                text = "${hueco.etiqueta} — $tipoLabel",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${hueco.anchoBase} × " +
                        "${hueco.largoBase} $unidad",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Color: $colorLabel",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            hueco.tipoMaterial?.let { material ->
                Text(
                    text = "Material: ${material.name}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}