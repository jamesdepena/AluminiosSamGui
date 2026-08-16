package edu.ucne.aluminiossamgui.domain.usecase.hueco

import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import kotlinx.coroutines.flow.Flow
import jakarta.inject.Inject

class ObserveHuecosByCasaIdUseCase @Inject constructor(
    private val repository: HuecoRepository
) {
    operator fun invoke(casaId: Int): Flow<List<Hueco>> {
        return repository.observeByCasaId(casaId)
    }
}