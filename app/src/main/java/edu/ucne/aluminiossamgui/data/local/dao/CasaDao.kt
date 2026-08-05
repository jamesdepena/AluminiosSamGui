package edu.ucne.aluminiossamgui.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.aluminiossamgui.data.local.entity.CasaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CasaDao {

    @Query("SELECT * FROM Casas ORDER BY casaId DESC")
    fun observeAll(): Flow<List<CasaEntity>>

    @Query("SELECT * FROM Casas WHERE casaId = :id LIMIT 1")
    suspend fun getById(id: Int): CasaEntity?

    @Upsert
    suspend fun upsert(casa: CasaEntity)

    @Query("DELETE FROM Casas WHERE casaId = :id")
    suspend fun delete(id: Int)
}