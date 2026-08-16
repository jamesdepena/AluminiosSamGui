package edu.ucne.aluminiossamgui.presentation.hueco.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.usecase.casa.GetCasaByIdUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.ObserveHuecosByCasaIdUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListHuecoViewModel @Inject constructor(
    private val getCasaByIdUseCase: GetCasaByIdUseCase,
    private val observeHuecosByCasaIdUseCase: ObserveHuecosByCasaIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListHuecoUiState())
    val state: StateFlow<ListHuecoUiState> = _state.asStateFlow()

    fun onEvent(event: ListHuecoUiEvent) {
        when (event) {
            is ListHuecoUiEvent.Load -> load(event.casaId)
            is ListHuecoUiEvent.FiltroChanged -> _state.update {
                it.copy(filtro = event.value)
            }
            is ListHuecoUiEvent.Edit -> Unit
            is ListHuecoUiEvent.CreateNew -> Unit
            is ListHuecoUiEvent.EditCasa -> Unit
        }
    }

    private fun load(casaId: Int) {
        viewModelScope.launch {
            val casa = getCasaByIdUseCase(casaId)

            _state.update {
                it.copy(casaId = casaId, nombreCasa = casa?.nombre ?: "Casa")
            }

            observeHuecosByCasaIdUseCase(casaId)
                .collect { huecos ->
                    _state.update { it.copy(huecos = huecos) }
                }
        }
    }
}