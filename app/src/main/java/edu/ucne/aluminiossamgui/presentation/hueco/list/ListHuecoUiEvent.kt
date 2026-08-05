package edu.ucne.aluminiossamgui.presentation.hueco.list

sealed interface ListHuecoUiEvent {
    data class Load(val casaId: Int) : ListHuecoUiEvent
    data class FiltroChanged(val value: String) : ListHuecoUiEvent
    data class Edit(val id: Int) : ListHuecoUiEvent
    data object CreateNew : ListHuecoUiEvent
    data object EditCasa : ListHuecoUiEvent
}