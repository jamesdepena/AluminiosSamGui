package edu.ucne.aluminiossamgui.presentation.auth

import edu.ucne.aluminiossamgui.domain.model.AuthUser

data class AuthUiState(
    val isLoading: Boolean = true,
    val user: AuthUser? = null,
    val errorMessage: String? = null
)