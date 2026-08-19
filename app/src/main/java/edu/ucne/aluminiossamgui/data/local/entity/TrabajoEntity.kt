package edu.ucne.aluminiossamgui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Trabajos")
data class TrabajoEntity(
    @PrimaryKey(autoGenerate = true)
    val trabajoId: Int = 0,
    val nombre: String,
    val nombreCliente: String? = null,
    val telefonoCliente: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)