package edu.ucne.aluminiossamgui.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Screen : NavKey {

    @Serializable
    data object CasaList : Screen()

    @Serializable
    data class CasaEdit(val id: Int? = null) : Screen()

    @Serializable
    data class HuecoList(val casaId: Int) : Screen()

    @Serializable
    data class HuecoEdit(val casaId: Int, val id: Int = 0) : Screen()
}