package edu.ucne.aluminiossamgui.presentation.casa.edit

sealed interface EditCasaUiEvent {
    data class Load(val id: Int?) : EditCasaUiEvent
    data class NombreChanged(val value: String) : EditCasaUiEvent
    data class DireccionChanged(val value: String) : EditCasaUiEvent
    data object Save : EditCasaUiEvent
    data object ShowDeleteDialog : EditCasaUiEvent
    data object DismissDeleteDialog : EditCasaUiEvent
    data object Delete : EditCasaUiEvent
}