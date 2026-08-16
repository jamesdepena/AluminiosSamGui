package edu.ucne.aluminiossamgui.domain.usecase.casa

import edu.ucne.aluminiossamgui.domain.repository.CasaRepository
import jakarta.inject.Inject

class DeleteCasaUseCase @Inject constructor(
    private val repository: CasaRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}