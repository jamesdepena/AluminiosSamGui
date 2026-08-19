package edu.ucne.aluminiossamgui.domain.model

data class AuthUser(
    val uid: String,
    val nombre: String? = null,
    val correo: String? = null,
    val fotoUrl: String? = null
)