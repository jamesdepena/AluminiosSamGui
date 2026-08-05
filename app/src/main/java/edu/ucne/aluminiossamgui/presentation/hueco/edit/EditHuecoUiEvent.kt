package edu.ucne.aluminiossamgui.presentation.hueco.edit

import edu.ucne.aluminiossamgui.domain.model.AcabadoPuerta
import edu.ucne.aluminiossamgui.domain.model.AnchoPuerta
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial

sealed interface EditHuecoUiEvent {
    data class Load(val casaId: Int, val huecoId: Int) : EditHuecoUiEvent
    data class EtiquetaChanged(val value: String) : EditHuecoUiEvent
    data class TipoChanged(val value: TipoHueco) : EditHuecoUiEvent
    data class AnchoBaseChanged(val value: String) : EditHuecoUiEvent
    data class LargoBaseChanged(val value: String) : EditHuecoUiEvent
    data class TipoMaterialChanged(val value: TipoMaterial) : EditHuecoUiEvent
    data class TresViasChanged(val value: Boolean) : EditHuecoUiEvent
    data class ColorChanged(val value: ColorMaterial) : EditHuecoUiEvent
    data class ColorPersonalizadoChanged(val value: String) : EditHuecoUiEvent
    data class AnchoPuertaChanged(val value: AnchoPuerta) : EditHuecoUiEvent
    data class AcabadoPuertaChanged(val value: AcabadoPuerta) : EditHuecoUiEvent
    data object Save : EditHuecoUiEvent
    data object ShowDeleteDialog : EditHuecoUiEvent
    data object DismissDeleteDialog : EditHuecoUiEvent
    data object Delete : EditHuecoUiEvent
}