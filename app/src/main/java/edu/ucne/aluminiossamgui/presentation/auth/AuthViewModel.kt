package edu.ucne.aluminiossamgui.presentation.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.aluminiossamgui.domain.usecase.auth.GetCurrentUserUseCase
import edu.ucne.aluminiossamgui.domain.usecase.auth.SignInWithGoogleUseCase
import edu.ucne.aluminiossamgui.domain.usecase.auth.SignOutUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    init {
        checkSession()
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.SignInWithGoogle -> signIn(event.context)
            is AuthUiEvent.SignOut -> signOut()
        }
    }

    private fun checkSession() {
        val user = getCurrentUserUseCase()

        _state.update {
            it.copy(isLoading = false, user = user, errorMessage = null)
        }
    }

    private fun signIn(context: Context) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = signInWithGoogleUseCase(context)

            result.onSuccess { user ->
                _state.update {
                    it.copy(isLoading = false, user = user, errorMessage = null)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo iniciar sesión."
                    )
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true, errorMessage = null)
            }

            val result = signOutUseCase()

            result.onSuccess {
                _state.update {
                    it.copy(isLoading = false, user = null)
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "No se pudo cerrar sesión."
                    )
                }
            }
        }
    }
}