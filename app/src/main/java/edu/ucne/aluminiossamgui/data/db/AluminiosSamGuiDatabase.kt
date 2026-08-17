package edu.ucne.aluminiossamgui.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.aluminiossamgui.data.local.dao.TrabajoDao
import edu.ucne.aluminiossamgui.data.local.dao.HuecoDao
import edu.ucne.aluminiossamgui.data.local.entity.TrabajoEntity
import edu.ucne.aluminiossamgui.data.local.entity.HuecoEntity

@Database(
    entities = [
        TrabajoEntity::class,
        HuecoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AluminiosSamGuiDatabase : RoomDatabase() {

    abstract fun trabajoDao(): TrabajoDao

    abstract fun huecoDao(): HuecoDao
}