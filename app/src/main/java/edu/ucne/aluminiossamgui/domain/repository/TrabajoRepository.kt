package edu.ucne.aluminiossamgui.domain.repository

import edu.ucne.aluminiossamgui.domain.model.Trabajo
import kotlinx.coroutines.flow.Flow

interface TrabajoRepository {
    fun observeTrabajos(): Flow<List<Trabajo>>
    suspend fun getById(id: Int): Trabajo?
    suspend fun upsert(trabajo: Trabajo)
    suspend fun delete(id: Int)
}