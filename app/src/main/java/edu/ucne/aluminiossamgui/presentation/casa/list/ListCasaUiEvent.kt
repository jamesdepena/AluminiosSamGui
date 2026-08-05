package edu.ucne.aluminiossamgui.presentation.casa.list

sealed interface ListCasaUiEvent {
    data class Edit(val id: Int) : ListCasaUiEvent
    data class OpenHuecos(val casaId: Int) : ListCasaUiEvent
    data class FiltroNombreChanged(val value: String) : ListCasaUiEvent
    data object CreateNew : ListCasaUiEvent
    data object Load : ListCasaUiEvent
}