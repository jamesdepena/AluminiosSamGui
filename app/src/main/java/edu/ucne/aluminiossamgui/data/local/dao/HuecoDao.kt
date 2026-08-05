package edu.ucne.aluminiossamgui.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.aluminiossamgui.data.local.entity.HuecoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HuecoDao {

    @Query(
        """
        SELECT * 
        FROM Huecos 
        WHERE casaId = :casaId 
        ORDER BY huecoId ASC
        """
    )
    fun observeByCasaId(casaId: Int): Flow<List<HuecoEntity>>

    @Query("SELECT * FROM Huecos WHERE huecoId = :id LIMIT 1")
    suspend fun getById(id: Int): HuecoEntity?

    @Upsert
    suspend fun upsert(hueco: HuecoEntity)

    @Query("DELETE FROM Huecos WHERE huecoId = :id")
    suspend fun delete(id: Int)
}