package edu.ucne.aluminiossamgui.presentation.hueco.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.GetTrabajoByIdUseCase
import edu.ucne.aluminiossamgui.domain.usecase.hueco.ObserveHuecosByTrabajoIdUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListHuecoViewModel @Inject constructor(
    private val getTrabajoByIdUseCase: GetTrabajoByIdUseCase,
    private val observeHuecosByTrabajoIdUseCase: ObserveHuecosByTrabajoIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListHuecoUiState())
    val state: StateFlow<ListHuecoUiState> = _state.asStateFlow()

    fun onEvent(event: ListHuecoUiEvent) {
        when (event) {
            is ListHuecoUiEvent.Load -> load(event.trabajoId)
            is ListHuecoUiEvent.FiltroChanged -> _state.update {
                it.copy(filtro = event.value)
            }
            is ListHuecoUiEvent.Edit -> Unit
            is ListHuecoUiEvent.CreateNew -> Unit
            is ListHuecoUiEvent.EditTrabajo -> Unit
        }
    }

    private fun load(trabajoId: Int) {
        viewModelScope.launch {
            val trabajo = getTrabajoByIdUseCase(trabajoId)

            _state.update {
                it.copy(trabajoId = trabajoId, nombreTrabajo = trabajo?.nombre ?: "Trabajo")
            }

            observeHuecosByTrabajoIdUseCase(trabajoId)
                .collect { huecos ->
                    _state.update { it.copy(huecos = huecos) }
                }
        }
    }
}