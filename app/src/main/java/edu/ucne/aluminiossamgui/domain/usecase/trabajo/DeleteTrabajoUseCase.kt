package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import jakarta.inject.Inject

class DeleteTrabajoUseCase @Inject constructor(
    private val repository: TrabajoRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}