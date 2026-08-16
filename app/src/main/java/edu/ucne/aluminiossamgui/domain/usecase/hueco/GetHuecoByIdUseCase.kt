package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import jakarta.inject.Inject

class GetHuecoByIdUseCase @Inject constructor(
    private val repository: HuecoRepository
) {
    suspend operator fun invoke(id: Int): Hueco? {
        return repository.getById(id)
    }
}