package edu.ucne.aluminiossamgui.domain.usecase.trabajo

import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class ObserveTrabajosUseCase @Inject constructor(
    private val repository: TrabajoRepository
) {
    operator fun invoke(): Flow<List<Trabajo>> {
        return repository.observeTrabajos()
    }
}