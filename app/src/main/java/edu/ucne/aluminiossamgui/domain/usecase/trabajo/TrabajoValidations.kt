package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.usecase.ValidationResult
import jakarta.inject.Inject

class TrabajoValidations @Inject constructor() {

    fun validateNombre(nombre: String): ValidationResult {
        if (nombre.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El nombre del trabajo no puede estar vacío."
            )
        }

        return ValidationResult(isValid = true)
    }
}