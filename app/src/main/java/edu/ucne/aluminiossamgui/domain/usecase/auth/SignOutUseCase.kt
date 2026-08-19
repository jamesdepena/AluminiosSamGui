package edu.ucne.aluminiossamgui.domain.usecase.auth

import edu.ucne.aluminiossamgui.domain.repository.AuthRepository
import jakarta.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return runCatching {
            repository.signOut()
        }
    }
}