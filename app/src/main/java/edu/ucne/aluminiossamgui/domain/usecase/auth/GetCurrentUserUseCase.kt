package edu.ucne.aluminiossamgui.domain.usecase.auth

import edu.ucne.aluminiossamgui.domain.model.AuthUser
import edu.ucne.aluminiossamgui.domain.repository.AuthRepository
import jakarta.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): AuthUser? {
        return repository.getCurrentUser()
    }
}