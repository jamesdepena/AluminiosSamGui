package edu.ucne.aluminiossamgui.domain.repository

import edu.ucne.aluminiossamgui.domain.model.Casa
import kotlinx.coroutines.flow.Flow

interface CasaRepository {
    fun observeCasas(): Flow<List<Casa>>
    suspend fun getById(id: Int): Casa?
    suspend fun upsert(casa: Casa)
    suspend fun delete(id: Int)
}