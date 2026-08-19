package edu.ucne.aluminiossamgui.domain.model

data class Trabajo(
    val trabajoId: Int = 0,
    val nombre: String,
    val nombreCliente: String? = null,
    val telefonoCliente: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)