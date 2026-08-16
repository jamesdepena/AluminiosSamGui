package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.CorteCorredera
import edu.ucne.aluminiossamgui.domain.model.DescuentoMaterial
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial
import jakarta.inject.Inject

class CalcularCorteCorrederaUseCase @Inject constructor() {

    operator fun invoke(hueco: Hueco): CorteCorredera? {
        if (hueco.tipo != TipoHueco.CORREDERA) {
            return null
        }

        val tipoMaterial = hueco.tipoMaterial ?: return null
        val descuento = getDescuento(tipoMaterial) ?: return null

        return CorteCorredera(
            cabezalYRielFijo =
                hueco.anchoBase + descuento.cabezalYRielFijo,

            lateral =
                hueco.largoBase + descuento.lateral,

            alfeizal =
                hueco.anchoBase + descuento.alfeizal,

            llavinYEnganche =
                hueco.largoBase + descuento.llavinYEnganche,

            tresViasAlf = if (hueco.esTresVias) {
                hueco.anchoBase + descuento.tresViasAlf
            } else {
                null
            }
        )
    }

    private fun getDescuento(
        tipoMaterial: TipoMaterial
    ): DescuentoMaterial? {
        return when (tipoMaterial) {
            TipoMaterial.TRADICIONAL -> DescuentoMaterial(
                tipoMaterial = TipoMaterial.TRADICIONAL,
                cabezalYRielFijo = -0.25,
                lateral = -0.5,
                alfeizal = -0.125,
                llavinYEnganche = -1.0,
                tresViasAlf = 0.5
            )

            TipoMaterial.P65 -> DescuentoMaterial(
                tipoMaterial = TipoMaterial.P65,
                cabezalYRielFijo = -1.375,
                lateral = -0.125,
                alfeizal = -0.6875,
                llavinYEnganche = -2.0,
                tresViasAlf = 0.5
            )

            TipoMaterial.P92 -> DescuentoMaterial(
                tipoMaterial = TipoMaterial.P92,
                cabezalYRielFijo = -1.625,
                lateral = -0.125,
                alfeizal = -0.5625,
                llavinYEnganche = -2.375,
                tresViasAlf = 1.0 / 3.0
            )

            TipoMaterial.P40 -> null
        }
    }
}