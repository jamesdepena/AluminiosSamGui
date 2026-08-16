package edu.ucne.aluminiossamgui.presentation.casa.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.usecase.casa.ObserveCasasUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ListCasaViewModel @Inject constructor(
    private val observeCasasUseCase: ObserveCasasUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ListCasaUiState())
    val state: StateFlow<ListCasaUiState> = _state.asStateFlow()

    init {
        onEvent(ListCasaUiEvent.Load)
    }

    fun onEvent(event: ListCasaUiEvent) {
        when (event) {
            is ListCasaUiEvent.Load -> observeCasas()
            is ListCasaUiEvent.FiltroNombreChanged -> _state.update {
                it.copy(filtroNombre = event.value)
            }
            is ListCasaUiEvent.Edit -> Unit
            is ListCasaUiEvent.OpenHuecos -> Unit
            is ListCasaUiEvent.CreateNew -> Unit
        }
    }

    private fun observeCasas() {
        viewModelScope.launch {
            observeCasasUseCase().collect { casas ->
                _state.update { it.copy(casas = casas) }
            }
        }
    }
}