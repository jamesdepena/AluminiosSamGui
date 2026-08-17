package edu.ucne.aluminiossamgui.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {

    @Serializable
    data object TrabajoList : Screen()

    @Serializable
    data class TrabajoEdit(val id: Int? = null) : Screen()

    @Serializable
    data class HuecoList(val trabajoId: Int) : Screen()

    @Serializable
    data class HuecoEdit(val trabajoId: Int, val id: Int = 0) : Screen()
}