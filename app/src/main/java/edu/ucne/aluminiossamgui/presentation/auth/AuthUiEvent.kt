package edu.ucne.aluminiossamgui.presentation.auth

import android.content.Context

sealed interface AuthUiEvent {
    data class SignInWithGoogle(val context: Context) : AuthUiEvent
    data object SignOut : AuthUiEvent
}