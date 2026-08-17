package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import jakarta.inject.Inject

class UpsertTrabajoUseCase @Inject constructor(
    private val repository: TrabajoRepository
) {
    suspend operator fun invoke(trabajo: Trabajo) {
        repository.upsert(trabajo)
    }
}