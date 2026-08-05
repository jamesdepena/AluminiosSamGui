package edu.ucne.aluminiossamgui.data.repository

import edu.ucne.aluminiossamgui.data.local.dao.HuecoDao
import edu.ucne.aluminiossamgui.data.mapper.toDomain
import edu.ucne.aluminiossamgui.data.mapper.toEntity
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.repository.HuecoRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HuecoRepositoryImpl @Inject constructor(
    private val huecoDao: HuecoDao
) : HuecoRepository {

    override fun observeByCasaId(casaId: Int): Flow<List<Hueco>> {
        return huecoDao.observeByCasaId(casaId).map { entities ->
            entities.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getById(id: Int): Hueco? {
        return huecoDao.getById(id)?.toDomain()
    }

    override suspend fun upsert(hueco: Hueco) {
        huecoDao.upsert(hueco.toEntity())
    }

    override suspend fun delete(id: Int) {
        huecoDao.delete(id)
    }
}