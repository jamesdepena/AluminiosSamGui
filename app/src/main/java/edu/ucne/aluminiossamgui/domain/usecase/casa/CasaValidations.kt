package edu.ucne.aluminiossamgui.domain.usecase.casa

import edu.ucne.aluminiossamgui.domain.usecase.ValidationResult
import javax.inject.Inject

class CasaValidations @Inject constructor() {

    fun validateNombre(nombre: String): ValidationResult {
        if (nombre.isBlank()) {
            return ValidationResult(
                isValid = false,
                errorMsg = "El nombre de la casa no puede estar vacío."
            )
        }

        return ValidationResult(isValid = true)
    }
}