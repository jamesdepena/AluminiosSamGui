package edu.ucne.aluminiossamgui.domain.usecase.casa

import edu.ucne.aluminiossamgui.domain.model.Casa
import edu.ucne.aluminiossamgui.domain.repository.CasaRepository
import jakarta.inject.Inject

class GetCasaByIdUseCase @Inject constructor(
    private val repository: CasaRepository
) {
    suspend operator fun invoke(id: Int): Casa? {
        return repository.getById(id)
    }
}