package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import jakarta.inject.Inject

class UpsertHuecoUseCase @Inject constructor(
    private val repository: HuecoRepository,
    private val validations: HuecoValidations
) {
    suspend operator fun invoke(hueco: Hueco): Result<Unit> {
        val validation = validations.validateHueco(hueco)

        if (!validation.isValid) {
            return Result.failure(
                IllegalArgumentException(
                    validation.errorMsg ?: "El hueco contiene datos inválidos."
                )
            )
        }

        return runCatching {
            repository.upsert(hueco)
        }
    }
}