package edu.ucne.aluminiossamgui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Trabajos")
data class TrabajoEntity(
    @PrimaryKey(autoGenerate = true)
    val trabajoId: Int = 0,
    val nombre: String,
    val direccion: String? = null
)