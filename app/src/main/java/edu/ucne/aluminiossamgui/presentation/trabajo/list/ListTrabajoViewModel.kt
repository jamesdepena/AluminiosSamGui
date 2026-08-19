package edu.ucne.aluminiossamgui.presentation.trabajo.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.ObserveTrabajosUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListTrabajoViewModel @Inject constructor(
    private val observeTrabajosUseCase: ObserveTrabajosUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListTrabajoUiState())
    val state: StateFlow<ListTrabajoUiState> = _state.asStateFlow()

    init {
        onEvent(ListTrabajoUiEvent.Load)
    }

    fun onEvent(event: ListTrabajoUiEvent) {
        when (event) {
            is ListTrabajoUiEvent.Load -> observeTrabajos()
            is ListTrabajoUiEvent.FiltroNombreChanged -> _state.update {
                it.copy(filtroNombre = event.value)
            }
            is ListTrabajoUiEvent.Edit -> Unit
            is ListTrabajoUiEvent.OpenHuecos -> Unit
            is ListTrabajoUiEvent.CreateNew -> Unit
            is ListTrabajoUiEvent.SignOut -> Unit
        }
    }

    private fun observeTrabajos() {
        viewModelScope.launch {
            observeTrabajosUseCase().collect { trabajos ->
                _state.update { it.copy(trabajos = trabajos) }
            }
        }
    }
}