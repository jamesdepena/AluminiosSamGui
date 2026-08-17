package edu.ucne.aluminiossamgui.presentation.trabajo.list

import edu.ucne.aluminiossamgui.domain.model.Trabajo

data class ListTrabajoUiState(
    val trabajos: List<Trabajo> = emptyList(),
    val filtroNombre: String = ""
) {
    val filtradas: List<Trabajo>
        get() = trabajos.filter { trabajo ->
            filtroNombre.isBlank() ||
                    trabajo.nombre.contains(other = filtroNombre, ignoreCase = true)
        }

    val totalTrabajos: Int get() = filtradas.size
}