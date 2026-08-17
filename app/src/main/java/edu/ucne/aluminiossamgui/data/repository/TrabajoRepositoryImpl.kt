package edu.ucne.aluminiossamgui.data.repository

import edu.ucne.aluminiossamgui.data.local.dao.TrabajoDao
import edu.ucne.aluminiossamgui.data.mapper.toDomain
import edu.ucne.aluminiossamgui.data.mapper.toEntity
import edu.ucne.aluminiossamgui.domain.model.Trabajo
import edu.ucne.aluminiossamgui.domain.repository.TrabajoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrabajoRepositoryImpl @Inject constructor(
    private val trabajoDao: TrabajoDao
) : TrabajoRepository {

    override fun observeTrabajos(): Flow<List<Trabajo>> {
        return trabajoDao.observeAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Trabajo? {
        return trabajoDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(trabajo: Trabajo) {
        trabajoDao.upsert(trabajo.toEntity())
    }

    override suspend fun delete(id: Int) {
        trabajoDao.delete(id)
    }
}