package edu.ucne.aluminiossamgui.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Casas")
data class CasaEntity(
    @PrimaryKey(autoGenerate = true)
    val casaId: Int = 0,
    val nombre: String,
    val direccion: String? = null
)