package edu.ucne.aluminiossamgui.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.aluminiossamgui.data.local.dao.CasaDao
import edu.ucne.aluminiossamgui.data.local.dao.HuecoDao
import edu.ucne.aluminiossamgui.data.local.entity.CasaEntity
import edu.ucne.aluminiossamgui.data.local.entity.HuecoEntity

@Database(
    entities = [
        CasaEntity::class,
        HuecoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AluminiosSamGuiDatabase : RoomDatabase() {

    abstract fun casaDao(): CasaDao

    abstract fun huecoDao(): HuecoDao
}