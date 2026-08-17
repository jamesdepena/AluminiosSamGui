package edu.ucne.aluminiossamgui.data.mapper

import edu.ucne.aluminiossamgui.data.local.entity.HuecoEntity
import edu.ucne.aluminiossamgui.domain.model.AcabadoPuerta
import edu.ucne.aluminiossamgui.domain.model.AnchoPuerta
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial

fun HuecoEntity.toDomain() = Hueco(
        huecoId = huecoId,
        trabajoId = trabajoId,
        etiqueta = etiqueta,
        tipo = TipoHueco.valueOf(tipo),
        anchoBase = anchoBase,
        largoBase = largoBase,
        tipoMaterial = if (tipoMaterial != null) {
            TipoMaterial.valueOf(tipoMaterial)
        } else {
            null
        },
        esTresVias = esTresVias,
        color = ColorMaterial.valueOf(color),
        colorPersonalizado = colorPersonalizado,
        anchoPuerta = getAnchoPuerta(anchoPuerta),
        acabadoPuerta = if (acabadoPuerta != null) {
            AcabadoPuerta.valueOf(acabadoPuerta)
        } else {
            null
        }
)

fun Hueco.toEntity() = HuecoEntity(
        huecoId = huecoId,
        trabajoId = trabajoId,
        etiqueta = etiqueta,
        tipo = tipo.name,
        anchoBase = anchoBase,
        largoBase = largoBase,
        tipoMaterial = tipoMaterial?.name,
        esTresVias = esTresVias,
        color = color.name,
        colorPersonalizado = colorPersonalizado,
        anchoPuerta = anchoPuerta?.centimetros,
        acabadoPuerta = acabadoPuerta?.name
)

private fun getAnchoPuerta(centimetros: Int?): AnchoPuerta? {
    return when (centimetros) {
        60 -> AnchoPuerta.ANCHO_60
        65 -> AnchoPuerta.ANCHO_65
        70 -> AnchoPuerta.ANCHO_70
        75 -> AnchoPuerta.ANCHO_75
        80 -> AnchoPuerta.ANCHO_80
        85 -> AnchoPuerta.ANCHO_85
        90 -> AnchoPuerta.ANCHO_90
        95 -> AnchoPuerta.ANCHO_95
        100 -> AnchoPuerta.ANCHO_100
        105 -> AnchoPuerta.ANCHO_105
        else -> null
    }
}