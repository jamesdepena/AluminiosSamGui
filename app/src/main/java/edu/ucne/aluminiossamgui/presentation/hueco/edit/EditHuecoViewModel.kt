package edu.ucne.aluminiossamgui.presentation.hueco.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial
import edu.ucne.aluminiossamgui.domain.usecase.hueco.CalcularCorteCorrederaUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.CalcularCorteCristalFijoUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.DeleteHuecoUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.GetHuecoByIdUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.HuecoValidations
import edu.ucne.aluminiossamgui.domain.usecase.hueco.UpsertHuecoUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditHuecoViewModel @Inject constructor(
    private val huecoValidations: HuecoValidations,
    private val getHuecoByIdUseCase: GetHuecoByIdUseCase,
    private val upsertHuecoUseCase: UpsertHuecoUseCase,
    private val deleteHuecoUseCase: DeleteHuecoUseCase,
    private val calcularCorteCorrederaUseCase: CalcularCorteCorrederaUseCase,
    private val calcularCorteCristalFijoUseCase: CalcularCorteCristalFijoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditHuecoUiState())
    val state: StateFlow<EditHuecoUiState> = _state.asStateFlow()

    fun onEvent(event: EditHuecoUiEvent) {
        when (event) {
            is EditHuecoUiEvent.Load -> { onLoad(event.casaId, event.huecoId) }
            is EditHuecoUiEvent.EtiquetaChanged -> {
                val validation = huecoValidations.validateEtiqueta(event.value)

                _state.update {
                    it.copy(etiqueta = event.value, etiquetaError = validation.errorMsg)
                }
            }
            is EditHuecoUiEvent.TipoChanged -> { onTipoChanged(event.value) }
            is EditHuecoUiEvent.AnchoBaseChanged -> {
                _state.update { it.copy(anchoBase = event.value, anchoBaseError = null) }
                updateCalculations()
            }
            is EditHuecoUiEvent.LargoBaseChanged -> {
                _state.update { it.copy(largoBase = event.value, largoBaseError = null) }
                updateCalculations()
            }
            is EditHuecoUiEvent.TipoMaterialChanged -> {
                _state.update { it.copy(tipoMaterial = event.value) }
                updateCalculations()
            }
            is EditHuecoUiEvent.TresViasChanged -> {
                _state.update { it.copy(esTresVias = event.value) }
                updateCalculations()
            }
            is EditHuecoUiEvent.ColorChanged -> {
                _state.update {
                    it.copy(
                        color = event.value,
                        colorPersonalizado =
                            if (event.value == ColorMaterial.OTRO) {
                                it.colorPersonalizado
                            } else {
                                ""
                            }
                    )
                }
            }
            is EditHuecoUiEvent.ColorPersonalizadoChanged -> {
                _state.update { it.copy(colorPersonalizado = event.value) }
            }
            is EditHuecoUiEvent.AnchoPuertaChanged -> {
                _state.update { it.copy(anchoPuerta = event.value) }
            }
            is EditHuecoUiEvent.AcabadoPuertaChanged -> {
                _state.update { it.copy(acabadoPuerta = event.value) }
            }
            is EditHuecoUiEvent.Save -> onSave()
            is EditHuecoUiEvent.ShowDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = true) }
            }
            is EditHuecoUiEvent.DismissDeleteDialog -> {
                _state.update { it.copy(showDeleteDialog = false) }
            }
            is EditHuecoUiEvent.Delete -> onDelete()
        }
    }

    private fun onTipoChanged(tipo: TipoHueco) {
        when (tipo) {
            TipoHueco.CORREDERA -> {
                _state.update {
                    it.copy(
                        tipo = tipo,
                        tipoMaterial = TipoMaterial.TRADICIONAL,
                        esTresVias = false,
                        color = ColorMaterial.NEGRO,
                        colorPersonalizado = "",
                        anchoPuerta = null,
                        acabadoPuerta = null,
                        errorMessage = null
                    )
                }
            }

            TipoHueco.PUERTA -> {
                _state.update {
                    it.copy(
                        tipo = tipo,
                        tipoMaterial = null,
                        esTresVias = false,
                        color = ColorMaterial.CAOBA,
                        colorPersonalizado = "",
                        anchoPuerta = null,
                        acabadoPuerta = null,
                        corteCorredera = null,
                        corteCristalFijo = null,
                        errorMessage = null
                    )
                }
            }

            TipoHueco.CRISTAL_FIJO -> {
                _state.update {
                    it.copy(
                        tipo = tipo,
                        tipoMaterial = TipoMaterial.P40,
                        esTresVias = false,
                        color = ColorMaterial.NEGRO,
                        colorPersonalizado = "",
                        anchoPuerta = null,
                        acabadoPuerta = null,
                        errorMessage = null
                    )
                }
            }
        }

        updateCalculations()
    }

    private fun onLoad(casaId: Int, huecoId: Int) {
        if (huecoId == 0) {
            _state.update { EditHuecoUiState(casaId = casaId, isNew = true) }
            return
        }

        viewModelScope.launch {
            val hueco = getHuecoByIdUseCase(huecoId)

            if (hueco == null) {
                _state.update {
                    it.copy(
                        casaId = casaId,
                        errorMessage = "No se encontró el hueco."
                    )
                }
                return@launch
            }

            _state.update {
                it.copy(
                    casaId = hueco.casaId,
                    huecoId = hueco.huecoId,
                    etiqueta = hueco.etiqueta,
                    tipo = hueco.tipo,
                    anchoBase = hueco.anchoBase.toString(),
                    largoBase = hueco.largoBase.toString(),
                    tipoMaterial = hueco.tipoMaterial,
                    esTresVias = hueco.esTresVias,
                    color = hueco.color,
                    colorPersonalizado =
                        hueco.colorPersonalizado ?: "",
                    anchoPuerta = hueco.anchoPuerta,
                    acabadoPuerta = hueco.acabadoPuerta,
                    isNew = false
                )
            }

            updateCalculations()
        }
    }

    private fun onSave() {
        val ancho = _state.value.anchoBase.toDoubleOrNull()
        val largo = _state.value.largoBase.toDoubleOrNull()

        val etiquetaValidation = huecoValidations.validateEtiqueta(_state.value.etiqueta)
        val anchoValidation = huecoValidations.validateAnchoBase(ancho ?: 0.0)
        val largoValidation = huecoValidations.validateLargoBase(largo ?: 0.0)

        if (
            !etiquetaValidation.isValid ||
            !anchoValidation.isValid ||
            !largoValidation.isValid
        ) {
            _state.update {
                it.copy(
                    etiquetaError = etiquetaValidation.errorMsg,
                    anchoBaseError = anchoValidation.errorMsg,
                    largoBaseError = largoValidation.errorMsg
                )
            }
            return
        }

        val hueco = createHueco(ancho = ancho ?: 0.0, largo = largo ?: 0.0)
        val huecoValidation = huecoValidations.validateHueco(hueco)
        if (!huecoValidation.isValid) {
            _state.update {
                it.copy(errorMessage = huecoValidation.errorMsg)
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(isSaving = true, errorMessage = null)
            }

            try {
                upsertHuecoUseCase(hueco)
                _state.update {
                    it.copy(isSaving = false, saved = true)
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message ?: "No se pudo guardar el hueco."
                    )
                }
            }
        }
    }

    private fun createHueco(
        ancho: Double,
        largo: Double
    ): Hueco {
        val colorPersonalizado =
            if (_state.value.colorPersonalizado.isBlank()) {
                null
            } else {
                _state.value.colorPersonalizado.trim()
            }

        return Hueco(
            huecoId = _state.value.huecoId ?: 0,
            casaId = _state.value.casaId,
            etiqueta = _state.value.etiqueta.trim(),
            tipo = _state.value.tipo,
            anchoBase = ancho,
            largoBase = largo,
            tipoMaterial = _state.value.tipoMaterial,
            esTresVias = _state.value.esTresVias,
            color = _state.value.color,
            colorPersonalizado = colorPersonalizado,
            anchoPuerta = _state.value.anchoPuerta,
            acabadoPuerta = _state.value.acabadoPuerta
        )
    }

    private fun updateCalculations() {
        val ancho = _state.value.anchoBase.toDoubleOrNull()
            ?: return
        val largo = _state.value.largoBase.toDoubleOrNull()
            ?: return

        if (ancho <= 0.0 || largo <= 0.0) {
            return
        }

        val hueco = createHueco(ancho, largo)

        _state.update {
            it.copy(
                corteCorredera =
                    calcularCorteCorrederaUseCase(hueco),
                corteCristalFijo =
                    calcularCorteCristalFijoUseCase(hueco)
            )
        }
    }

    private fun onDelete() {
        val id = _state.value.huecoId ?: return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isDeleting = true,
                    showDeleteDialog = false,
                    errorMessage = null
                )
            }

            try {
                deleteHuecoUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message ?: "No se pudo eliminar el hueco."
                    )
                }
            }
        }
    }
}