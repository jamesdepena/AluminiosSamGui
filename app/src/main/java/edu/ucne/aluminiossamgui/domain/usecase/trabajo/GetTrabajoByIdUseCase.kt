package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import jakarta.inject.Inject

class GetTrabajoByIdUseCase @Inject constructor(
    private val repository: TrabajoRepository
) {
    suspend operator fun invoke(id: Int): Trabajo? {
        return repository.getById(id)
    }
}