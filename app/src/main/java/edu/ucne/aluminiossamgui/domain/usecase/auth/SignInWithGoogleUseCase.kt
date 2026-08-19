package edu.ucne.aluminiossamgui.domain.usecase.auth

import android.content.Context
import edu.ucne.aluminiossamgui.domain.model.AuthUser
import edu.ucne.aluminiossamgui.domain.repository.AuthRepository
import jakarta.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(context: Context): Result<AuthUser> {
        return repository.signInWithGoogle(context)
    }
}