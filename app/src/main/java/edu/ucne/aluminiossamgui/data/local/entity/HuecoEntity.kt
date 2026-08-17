package edu.ucne.aluminiossamgui.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Huecos",
    foreignKeys = [
        ForeignKey(
            entity = TrabajoEntity::class,
            parentColumns = ["trabajoId"],
            childColumns = ["trabajoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["trabajoId"])
    ]
)
data class HuecoEntity(
    @PrimaryKey(autoGenerate = true)
    val huecoId: Int = 0,
    val trabajoId: Int,
    val etiqueta: String,
    val tipo: String,
    val anchoBase: Double,
    val largoBase: Double,
    val tipoMaterial: String? = null,
    val esTresVias: Boolean = false,
    val color: String,
    val colorPersonalizado: String? = null,
    val anchoPuerta: Int? = null,
    val acabadoPuerta: String? = null
)