package edu.ucne.aluminiossamgui.domain.repository

import edu.ucne.aluminiossamgui.domain.model.Hueco
import kotlinx.coroutines.flow.Flow

interface HuecoRepository {
    fun observeByCasaId(casaId: Int): Flow<List<Hueco>>
    suspend fun getById(id: Int): Hueco?
    suspend fun upsert(hueco: Hueco)
    suspend fun delete(id: Int)
}