package edu.ucne.aluminiossamgui.presentation.trabajo.list

sealed interface ListTrabajoUiEvent {
    data class Edit(val id: Int) : ListTrabajoUiEvent
    data class OpenHuecos(val trabajoId: Int) : ListTrabajoUiEvent
    data class FiltroNombreChanged(val value: String) : ListTrabajoUiEvent
    data object CreateNew : ListTrabajoUiEvent
    data object Load : ListTrabajoUiEvent
}