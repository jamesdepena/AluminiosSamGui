package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import jakarta.inject.Inject

class UpsertTrabajoUseCase @Inject constructor(
    private val repository: TrabajoRepository,
    private val validations: TrabajoValidations
) {
    suspend operator fun invoke(trabajo: Trabajo): Result<Unit> {
        val validation = validations.validateNombre(trabajo.nombre)

        if (!validation.isValid) {
            return Result.failure(
                IllegalArgumentException(
                    validation.errorMsg ?: "El trabajo contiene datos inválidos."
                )
            )
        }

        return runCatching {
            repository.upsert(trabajo)
        }
    }
}