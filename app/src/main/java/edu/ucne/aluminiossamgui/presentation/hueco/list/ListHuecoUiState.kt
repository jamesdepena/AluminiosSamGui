package edu.ucne.aluminiossamgui.presentation.hueco.list

import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco

data class ListHuecoUiState(
    val trabajoId: Int? = null,
    val nombreTrabajo: String = "",
    val huecos: List<Hueco> = emptyList(),
    val filtro: String = ""
) {
    val filtrados: List<Hueco>
        get() = huecos.filter { hueco ->
            filtro.isBlank() ||
                    hueco.etiqueta.contains(
                        other = filtro,
                        ignoreCase = true
                    ) ||
                    getTipoLabel(hueco.tipo).contains(
                        other = filtro,
                        ignoreCase = true
                    )
        }

    val totalHuecos: Int get() = filtrados.size

    private fun getTipoLabel(tipo: TipoHueco): String {
        return when (tipo) {
            TipoHueco.CORREDERA -> "Corredera"
            TipoHueco.PUERTA -> "Puerta"
            TipoHueco.CRISTAL_FIJO -> "Cristal fijo"
        }
    }
}