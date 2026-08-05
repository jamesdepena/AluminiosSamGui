package edu.ucne.aluminiossamgui.domain.usecase

data class ValidationResult(
    val isValid: Boolean = false,
    val errorMsg: String? = null
)