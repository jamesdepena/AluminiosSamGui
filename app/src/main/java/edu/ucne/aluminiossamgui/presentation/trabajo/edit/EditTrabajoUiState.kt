package edu.ucne.aluminiossamgui.presentation.trabajo.edit

data class EditTrabajoUiState(
    val trabajoId: Int? = null,
    val nombre: String = "",
    val direccion: String = "",

    val nombreError: String? = null,
    val errorMessage: String? = null,

    val isNew: Boolean = true,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,

    val showDeleteDialog: Boolean = false,

    val saved: Boolean = false,
    val deleted: Boolean = false
)