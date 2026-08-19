package edu.ucne.aluminiossamgui.presentation.trabajo.edit

sealed interface EditTrabajoUiEvent {
    data class Load(val id: Int?) : EditTrabajoUiEvent
    data class NombreChanged(val value: String) : EditTrabajoUiEvent
    data class NombreClienteChanged(val value: String) : EditTrabajoUiEvent
    data class TelefonoClienteChanged(val value: String) : EditTrabajoUiEvent
    data class DireccionChanged(val value: String) : EditTrabajoUiEvent
    data class NotasChanged(val value: String) : EditTrabajoUiEvent
    data object Save : EditTrabajoUiEvent
    data object ShowDeleteDialog : EditTrabajoUiEvent
    data object DismissDeleteDialog : EditTrabajoUiEvent
    data object Delete : EditTrabajoUiEvent
}