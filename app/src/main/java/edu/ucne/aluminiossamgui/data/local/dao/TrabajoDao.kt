package edu.ucne.aluminiossamgui.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.aluminiossamgui.data.local.entity.TrabajoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrabajoDao {

    @Query("SELECT * FROM Trabajos ORDER BY trabajoId DESC")
    fun observeAll(): Flow<List<TrabajoEntity>>

    @Query("SELECT * FROM Trabajos WHERE trabajoId = :id LIMIT 1")
    suspend fun getById(id: Int): TrabajoEntity?

    @Upsert
    suspend fun upsert(trabajo: TrabajoEntity)

    @Query("DELETE FROM Trabajos WHERE trabajoId = :id")
    suspend fun delete(id: Int)
}