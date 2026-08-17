package edu.ucne.aluminiossamgui.presentation.trabajo.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.TrabajoValidations
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.DeleteTrabajoUseCase
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.GetTrabajoByIdUseCase
import edu.ucne.aluminiossamgui.domain.usecase.trabajo.UpsertTrabajoUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditTrabajoViewModel @Inject constructor(
    private val trabajoValidations: TrabajoValidations,
    private val getTrabajoByIdUseCase: GetTrabajoByIdUseCase,
    private val upsertTrabajoUseCase: UpsertTrabajoUseCase,
    private val deleteTrabajoUseCase: DeleteTrabajoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditTrabajoUiState())
    val state: StateFlow<EditTrabajoUiState> = _state.asStateFlow()

    fun onEvent(event: EditTrabajoUiEvent) {
        when (event) {
            is EditTrabajoUiEvent.Load -> onLoad(event.id)
            is EditTrabajoUiEvent.NombreChanged -> onNombreChanged(event.value)
            is EditTrabajoUiEvent.DireccionChanged -> _state.update {
                it.copy(direccion = event.value)
            }
            is EditTrabajoUiEvent.Save -> onSave()
            is EditTrabajoUiEvent.ShowDeleteDialog -> _state.update {
                it.copy(showDeleteDialog = true)
            }
            is EditTrabajoUiEvent.DismissDeleteDialog -> _state.update {
                it.copy(showDeleteDialog = false)
            }
            is EditTrabajoUiEvent.Delete -> onDelete()
        }
    }

    private fun onNombreChanged(nombre: String) {
        val validation = trabajoValidations.validateNombre(nombre)
        _state.update { it.copy(nombre = nombre, nombreError = validation.errorMsg) }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { EditTrabajoUiState(isNew = true, trabajoId = null) }
            return
        }

        viewModelScope.launch {
            val trabajo = getTrabajoByIdUseCase(id)

            if (trabajo != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        trabajoId = trabajo.trabajoId,
                        nombre = trabajo.nombre,
                        direccion = trabajo.direccion ?: ""
                    )
                }
            } else {
                _state.update {
                    it.copy(errorMessage = "No se encontró el trabajo.")
                }
            }
        }
    }

    private fun onSave() {
        val nombreValidation = trabajoValidations.validateNombre(_state.value.nombre)

        if (!nombreValidation.isValid) {
            _state.update { it.copy(nombreError = nombreValidation.errorMsg) }
            return
        }

        val direccion = if (_state.value.direccion.isBlank()) {
            null
        } else {
            _state.value.direccion.trim()
        }

        val trabajo = Trabajo(
            trabajoId = _state.value.trabajoId ?: 0,
            nombre = _state.value.nombre.trim(),
            direccion = direccion
        )

        viewModelScope.launch {
            _state.update {
                it.copy(isSaving = true, errorMessage = null)
            }

            val result = upsertTrabajoUseCase(trabajo)

            result.onSuccess {
                _state.update {
                    it.copy(isSaving = false, saved = true)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: "No se pudo guardar el trabajo."
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.trabajoId ?: return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isDeleting = true,
                    showDeleteDialog = false
                )
            }

            deleteTrabajoUseCase(id)

            _state.update {
                it.copy(
                    isDeleting = false,
                    deleted = true
                )
            }
        }
    }
}