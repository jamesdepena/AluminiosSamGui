package edu.ucne.aluminiossamgui.data.mapper

import com.google.firebase.auth.FirebaseUser
import edu.ucne.aluminiossamgui.domain.model.AuthUser

fun FirebaseUser.toDomain(): AuthUser {
    return AuthUser(
        uid = uid,
        nombre = displayName,
        correo = email,
        fotoUrl = photoUrl?.toString()
    )
}