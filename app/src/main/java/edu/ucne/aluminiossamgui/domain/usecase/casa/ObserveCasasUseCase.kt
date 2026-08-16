package edu.ucne.aluminiossamgui.domain.usecase.casa

import edu.ucne.aluminiossamgui.domain.model.Casa
import edu.ucne.aluminiossamgui.domain.repository.CasaRepository
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class ObserveCasasUseCase @Inject constructor(
    private val repository: CasaRepository
) {
    operator fun invoke(): Flow<List<Casa>> {
        return repository.observeCasas()
    }
}