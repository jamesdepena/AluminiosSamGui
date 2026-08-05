package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import javax.inject.Inject

class DeleteHuecoUseCase @Inject constructor(
    private val repository: HuecoRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.delete(id)
    }
}