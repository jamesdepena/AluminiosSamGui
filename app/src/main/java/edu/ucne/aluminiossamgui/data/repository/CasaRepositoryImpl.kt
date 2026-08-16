package edu.ucne.aluminiossamgui.data.repository

import edu.ucne.aluminiossamgui.data.local.dao.CasaDao
import edu.ucne.aluminiossamgui.data.mapper.toDomain
import edu.ucne.aluminiossamgui.data.mapper.toEntity
import edu.ucne.aluminiossamgui.domain.model.Casa
import edu.ucne.aluminiossamgui.domain.repository.CasaRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CasaRepositoryImpl @Inject constructor(
    private val casaDao: CasaDao
) : CasaRepository {

    override fun observeCasas(): Flow<List<Casa>> {
        return casaDao.observeAll().map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Casa? {
        return casaDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(casa: Casa) {
        casaDao.upsert(casa.toEntity())
    }

    override suspend fun delete(id: Int) {
        casaDao.delete(id)
    }
}