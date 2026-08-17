package edu.ucne.aluminiossamgui.domain.repository

import edu.ucne.aluminiossamgui.domain.model.Hueco
import kotlinx.coroutines.flow.Flow

interface HuecoRepository {
    fun observeByTrabajoId(trabajoId: Int): Flow<List<Hueco>>
    suspend fun getById(id: Int): Hueco?
    suspend fun upsert(hueco: Hueco)
    suspend fun delete(id: Int)
}