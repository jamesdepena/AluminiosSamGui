package edu.ucne.aluminiossamgui.presentation.hueco.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.aluminiossamgui.domain.model.AcabadoPuerta
import edu.ucne.aluminiossamgui.domain.model.AnchoPuerta
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial

@Composable
fun EditHuecoScreen(
    casaId: Int,
    huecoId: Int,
    viewModel: EditHuecoViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(casaId, huecoId) {
        viewModel.onEvent(EditHuecoUiEvent.Load(casaId = casaId, huecoId = huecoId))
    }

    LaunchedEffect(state.saved, state.deleted) {
        if (state.saved || state.deleted) {
            onBack()
        }
    }

    EditHuecoBody(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHuecoBody(
    state: EditHuecoUiState,
    onEvent: (EditHuecoUiEvent) -> Unit,
    onBack: () -> Unit
) {
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(EditHuecoUiEvent.DismissDeleteDialog) },
            title = { Text("Eliminar hueco") },
            text = { Text("¿Deseas eliminar este hueco?") },
            confirmButton = {
                TextButton(
                    onClick = { onEvent(EditHuecoUiEvent.Delete) }
                ) {
                    Text(text = "Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onEvent(EditHuecoUiEvent.DismissDeleteDialog) }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (state.isNew) { "Nuevo Hueco" } else { "Editar Hueco" }) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 32.dp)
        ) {
            OutlinedTextField(
                value = state.etiqueta,
                onValueChange = { onEvent(EditHuecoUiEvent.EtiquetaChanged(it)) },
                label = { Text("Etiqueta") },
                isError = state.etiquetaError != null,
                supportingText = { state.etiquetaError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            TipoHuecoSelector(
                value = state.tipo,
                onValueChanged = { onEvent(EditHuecoUiEvent.TipoChanged(it)) }
            )

            val unidad = if (state.tipo == TipoHueco.PUERTA) {
                "cm"
            } else {
                "pulgadas"
            }

            OutlinedTextField(
                value = state.anchoBase,
                onValueChange = { onEvent(EditHuecoUiEvent.AnchoBaseChanged(it)) },
                label = { Text("Ancho real del hueco ($unidad)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.anchoBaseError != null,
                supportingText = { state.anchoBaseError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.largoBase,
                onValueChange = { onEvent(EditHuecoUiEvent.LargoBaseChanged(it)) },
                label = { Text("Alto real del hueco ($unidad)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = state.largoBaseError != null,
                supportingText = { state.largoBaseError?.let { Text(it) } },
                modifier = Modifier.fillMaxWidth()
            )

            when (state.tipo) {
                TipoHueco.CORREDERA -> {
                    CorrederaFields(state, onEvent)
                }

                TipoHueco.PUERTA -> {
                    PuertaFields(state, onEvent)
                }

                TipoHueco.CRISTAL_FIJO -> {
                    CristalFijoFields(state, onEvent)
                }
            }

            state.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = { onEvent(EditHuecoUiEvent.Save) },
                    enabled = !state.isSaving && !state.isDeleting
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Text(if (state.isSaving) "Guardando..." else "Guardar")
                }

                if (!state.isNew) {
                    OutlinedButton(
                        onClick = { onEvent(EditHuecoUiEvent.ShowDeleteDialog) },
                        enabled = !state.isSaving && !state.isDeleting,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor =
                                MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
private fun CorrederaFields(
    state: EditHuecoUiState,
    onEvent: (EditHuecoUiEvent) -> Unit
) {
    MaterialSelector(
        value = state.tipoMaterial ?: TipoMaterial.TRADICIONAL,
        onValueChanged = { onEvent(EditHuecoUiEvent.TipoMaterialChanged(it)) }
    )

    Row {
        Checkbox(
            checked = state.esTresVias,
            onCheckedChange = { onEvent(EditHuecoUiEvent.TresViasChanged(it)) }
        )
        Text(
            text = "Corredera de tres vías",
            modifier = Modifier.padding(top = 12.dp)
        )
    }

    ColorSelector(
        colors = listOf(
            ColorMaterial.NEGRO,
            ColorMaterial.BLANCO,
            ColorMaterial.CAOBA,
            ColorMaterial.GRIS
        ),
        value = state.color,
        onValueChanged = { onEvent(EditHuecoUiEvent.ColorChanged(it)) }
    )

    state.corteCorredera?.let { corte ->
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Cálculo de corte")
                Text("Cabezal/riel: ${corte.cabezalYRielFijo}")
                Text("Lateral: ${corte.lateral}")
                Text("Alfeizal: ${corte.alfeizal}")
                Text("Llavín/enganche: ${corte.llavinYEnganche}")

                corte.tresViasAlf?.let {
                    Text("Tres vías ALF: $it")
                }
            }
        }
    }
}

@Composable
private fun PuertaFields(
    state: EditHuecoUiState,
    onEvent: (EditHuecoUiEvent) -> Unit
) {
    AnchoPuertaSelector(
        value = state.anchoPuerta,
        onValueChanged = { onEvent(EditHuecoUiEvent.AnchoPuertaChanged(it)) }
    )

    AcabadoSelector(
        value = state.acabadoPuerta,
        onValueChanged = { onEvent(EditHuecoUiEvent.AcabadoPuertaChanged(it)) }
    )

    ColorSelector(
        colors = listOf(
            ColorMaterial.CAOBA,
            ColorMaterial.BLANCO,
            ColorMaterial.OTRO
        ),
        value = state.color,
        onValueChanged = { onEvent(EditHuecoUiEvent.ColorChanged(it)) }
    )

    if (state.color == ColorMaterial.OTRO) {
        OutlinedTextField(
            value = state.colorPersonalizado,
            onValueChange = { onEvent(EditHuecoUiEvent.ColorPersonalizadoChanged(it)) },
            label = { Text("Color personalizado") },
            modifier = Modifier.fillMaxWidth()
        )
    }

    Text("Alto estándar de la puerta: 210 cm")
}

@Composable
private fun CristalFijoFields(
    state: EditHuecoUiState,
    onEvent: (EditHuecoUiEvent) -> Unit
) {
    Text("Material: P40")
    Text("Vidrio: 1/4\"")

    ColorSelector(
        colors = listOf(
            ColorMaterial.NEGRO,
            ColorMaterial.BLANCO,
            ColorMaterial.GRIS,
            ColorMaterial.CAOBA
        ),
        value = state.color,
        onValueChanged = { onEvent(EditHuecoUiEvent.ColorChanged(it)) }
    )

    state.corteCristalFijo?.let { corte ->
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("Cálculo de corte")
                Text(
                    "Marco: ${corte.anchoMarco} × " +
                            "${corte.largoMarco}"
                )
                Text(
                    "Vidrio: ${corte.anchoVidrio} × " +
                            "${corte.largoVidrio}"
                )
            }
        }
    }
}

@Composable
private fun TipoHuecoSelector(
    value: TipoHueco,
    onValueChanged: (TipoHueco) -> Unit
) {
    val values = TipoHueco.values()
    val labels = listOf("Corredera", "Puerta", "Cristal fijo")

    SimpleSelector(
        label = "Tipo de hueco",
        selected = labels[values.indexOf(value)],
        options = labels,
        onSelected = { index -> onValueChanged(values[index]) }
    )
}

@Composable
private fun MaterialSelector(
    value: TipoMaterial,
    onValueChanged: (TipoMaterial) -> Unit
) {
    val values = arrayOf(
        TipoMaterial.TRADICIONAL,
        TipoMaterial.P65,
        TipoMaterial.P92
    )

    SimpleSelector(
        label = "Material",
        selected = value.name,
        options = values.map { it.name },
        onSelected = { index -> onValueChanged(values[index]) }
    )
}

@Composable
private fun ColorSelector(
    colors: List<ColorMaterial>,
    value: ColorMaterial,
    onValueChanged: (ColorMaterial) -> Unit
) {
    SimpleSelector(
        label = "Color",
        selected = value.name,
        options = colors.map { it.name },
        onSelected = { index -> onValueChanged(colors[index]) }
    )
}

@Composable
private fun AnchoPuertaSelector(
    value: AnchoPuerta?,
    onValueChanged: (AnchoPuerta) -> Unit
) {
    val values = AnchoPuerta.values()

    SimpleSelector(
        label = "Ancho estándar",
        selected = value?.let { "${it.centimetros} cm" } ?: "Seleccionar",
        options = values.map { "${it.centimetros} cm" },
        onSelected = { index -> onValueChanged(values[index]) }
    )
}

@Composable
private fun AcabadoSelector(
    value: AcabadoPuerta?,
    onValueChanged: (AcabadoPuerta) -> Unit
) {
    val values = AcabadoPuerta.values()
    val labels = listOf("Lisa", "Diseño")

    SimpleSelector(
        label = "Acabado",
        selected = if (value == null) {
            "Seleccionar"
        } else {
            labels[values.indexOf(value)]
        },
        options = labels,
        onSelected = { index -> onValueChanged(values[index]) }
    )
}

@Composable
private fun SimpleSelector(
    label: String,
    selected: String,
    options: List<String>,
    onSelected: (Int) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )

        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selected)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(option)
                    },
                    onClick = {
                        expanded = false
                        onSelected(index)
                    }
                )
            }
        }
    }
}