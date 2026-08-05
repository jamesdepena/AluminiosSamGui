package edu.ucne.aluminiossamgui.presentation.casa.list

import edu.ucne.aluminiossamgui.domain.model.Casa

data class ListCasaUiState(
    val casas: List<Casa> = emptyList(),
    val filtroNombre: String = ""
) {
    val filtradas: List<Casa>
        get() = casas.filter { casa ->
            filtroNombre.isBlank() ||
                    casa.nombre.contains(other = filtroNombre, ignoreCase = true)
        }

    val totalCasas: Int get() = filtradas.size
}