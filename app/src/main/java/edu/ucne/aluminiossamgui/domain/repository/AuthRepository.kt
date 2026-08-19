package edu.ucne.aluminiossamgui.domain.repository

import android.content.Context
import edu.ucne.aluminiossamgui.domain.model.AuthUser

interface AuthRepository {
    suspend fun signInWithGoogle(context: Context): Result<AuthUser>
    suspend fun signOut()
    fun getCurrentUser(): AuthUser?
}