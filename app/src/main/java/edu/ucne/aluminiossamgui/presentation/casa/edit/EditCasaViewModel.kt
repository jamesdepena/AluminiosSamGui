package edu.ucne.aluminiossamgui.presentation.casa.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.model.Casa
import edu.ucne.aluminiossamgui.domain.usecase.casa.CasaValidations
import edu.ucne.aluminiossamgui.domain.usecase.casa.DeleteCasaUseCase
import edu.ucne.aluminiossamgui.domain.usecase.casa.GetCasaByIdUseCase
import edu.ucne.aluminiossamgui.domain.usecase.casa.UpsertCasaUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditCasaViewModel @Inject constructor(
    private val casaValidations: CasaValidations,
    private val getCasaByIdUseCase: GetCasaByIdUseCase,
    private val upsertCasaUseCase: UpsertCasaUseCase,
    private val deleteCasaUseCase: DeleteCasaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(EditCasaUiState())
    val state: StateFlow<EditCasaUiState> = _state.asStateFlow()

    fun onEvent(event: EditCasaUiEvent) {
        when (event) {
            is EditCasaUiEvent.Load -> onLoad(event.id)
            is EditCasaUiEvent.NombreChanged -> onNombreChanged(event.value)
            is EditCasaUiEvent.DireccionChanged -> _state.update {
                it.copy(direccion = event.value)
            }
            is EditCasaUiEvent.Save -> onSave()
            is EditCasaUiEvent.ShowDeleteDialog -> _state.update {
                it.copy(showDeleteDialog = true)
            }
            is EditCasaUiEvent.DismissDeleteDialog -> _state.update {
                it.copy(showDeleteDialog = false)
            }
            is EditCasaUiEvent.Delete -> onDelete()
        }
    }

    private fun onNombreChanged(nombre: String) {
        val validation = casaValidations.validateNombre(nombre)
        _state.update { it.copy(nombre = nombre, nombreError = validation.errorMsg) }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { EditCasaUiState(isNew = true, casaId = null) }
            return
        }

        viewModelScope.launch {
            val casa = getCasaByIdUseCase(id)

            if (casa != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        casaId = casa.casaId,
                        nombre = casa.nombre,
                        direccion = casa.direccion ?: ""
                    )
                }
            } else {
                _state.update {
                    it.copy(errorMessage = "No se encontró la casa.")
                }
            }
        }
    }

    private fun onSave() {
        val nombreValidation = casaValidations.validateNombre(_state.value.nombre)

        if (!nombreValidation.isValid) {
            _state.update { it.copy(nombreError = nombreValidation.errorMsg) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null) }

            try {
                val direccion = if (_state.value.direccion.isBlank()) {
                    null
                } else {
                    _state.value.direccion.trim()
                }

                val casa = Casa(
                    casaId = _state.value.casaId ?: 0,
                    nombre = _state.value.nombre.trim(),
                    direccion = direccion
                )

                upsertCasaUseCase(casa)
                _state.update { it.copy(isSaving = false, saved = true) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = e.message
                            ?: "No se pudo guardar la casa."
                    )
                }
            }
        }
    }

    private fun onDelete() {
        val id = _state.value.casaId ?: return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isDeleting = true,
                    showDeleteDialog = false,
                    errorMessage = null
                )
            }

            try {
                deleteCasaUseCase(id)
                _state.update { it.copy(isDeleting = false, deleted = true) }

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isDeleting = false,
                        errorMessage = e.message
                            ?: "No se pudo eliminar la casa."
                    )
                }
            }
        }
    }
}