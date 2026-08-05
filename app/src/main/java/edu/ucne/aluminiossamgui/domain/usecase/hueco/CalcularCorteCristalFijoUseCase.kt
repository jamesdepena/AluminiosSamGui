package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.CorteCristalFijo
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import javax.inject.Inject

class CalcularCorteCristalFijoUseCase @Inject constructor() {

    operator fun invoke(hueco: Hueco): CorteCristalFijo? {
        if (hueco.tipo != TipoHueco.CRISTAL_FIJO) {
            return null
        }

        val descuentoVidrio = 0.25

        return CorteCristalFijo(
            anchoMarco = hueco.anchoBase,
            largoMarco = hueco.largoBase,
            anchoVidrio = hueco.anchoBase - descuentoVidrio,
            largoVidrio = hueco.largoBase - descuentoVidrio
        )
    }
}