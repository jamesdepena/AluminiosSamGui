package edu.ucne.aluminiossamgui.domain.model

data class Hueco(
    val huecoId: Int = 0,
    val trabajoId: Int,
    val etiqueta: String,
    val tipo: TipoHueco,
    val anchoBase: Double,
    val largoBase: Double,
    val tipoMaterial: TipoMaterial? = null,
    val esTresVias: Boolean = false,
    val color: ColorMaterial,
    val colorPersonalizado: String? = null,
    val anchoPuerta: AnchoPuerta? = null,
    val acabadoPuerta: AcabadoPuerta? = null
)