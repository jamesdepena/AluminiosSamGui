package edu.ucne.aluminiossamgui.presentation.hueco.edit

import edu.ucne.aluminiossamgui.domain.model.AcabadoPuerta
import edu.ucne.aluminiossamgui.domain.model.AnchoPuerta
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.CorteCorredera
import edu.ucne.aluminiossamgui.domain.model.CorteCristalFijo
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial

data class EditHuecoUiState(
    val trabajoId: Int = 0,
    val huecoId: Int? = null,

    val etiqueta: String = "",
    val tipo: TipoHueco = TipoHueco.CORREDERA,
    val anchoBase: String = "",
    val largoBase: String = "",

    val tipoMaterial: TipoMaterial? = TipoMaterial.TRADICIONAL,
    val esTresVias: Boolean = false,
    val color: ColorMaterial = ColorMaterial.NEGRO,
    val colorPersonalizado: String = "",

    val anchoPuerta: AnchoPuerta? = null,
    val acabadoPuerta: AcabadoPuerta? = null,

    val etiquetaError: String? = null,
    val anchoBaseError: String? = null,
    val largoBaseError: String? = null,
    val errorMessage: String? = null,

    val corteCorredera: CorteCorredera? = null,
    val corteCristalFijo: CorteCristalFijo? = null,

    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val saved: Boolean = false,
    val deleted: Boolean = false
)