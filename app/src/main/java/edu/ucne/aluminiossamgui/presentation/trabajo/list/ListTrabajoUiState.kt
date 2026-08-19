package edu.ucne.aluminiossamgui.presentation.trabajo.list

import edu.ucne.aluminiossamgui.domain.model.Trabajo

data class ListTrabajoUiState(
    val trabajos: List<Trabajo> = emptyList(),
    val filtroNombre: String = ""
) {
    val filtrados: List<Trabajo>
        get() = trabajos.filter { trabajo ->
            filtroNombre.isBlank() ||
                    trabajo.nombre.contains(
                        filtroNombre,
                        ignoreCase = true
                    ) ||
                    trabajo.nombreCliente?.contains(
                        filtroNombre,
                        ignoreCase = true
                    ) == true ||
                    trabajo.telefonoCliente?.contains(
                        filtroNombre,
                        ignoreCase = true
                    ) == true ||
                    trabajo.direccion?.contains(
                        filtroNombre,
                        ignoreCase = true
                    ) == true
        }

    val totalTrabajos: Int get() = filtrados.size
}