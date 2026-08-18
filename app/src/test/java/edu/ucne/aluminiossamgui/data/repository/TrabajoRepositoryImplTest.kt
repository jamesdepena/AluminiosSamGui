package edu.ucne.aluminiossamgui.data.repository

import edu.ucne.aluminiossamgui.data.local.dao.TrabajoDao
import edu.ucne.aluminiossamgui.data.local.entity.TrabajoEntity
import edu.ucne.aluminiossamgui.domain.model.Trabajo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class TrabajoRepositoryImplTest {

    private lateinit var repository: TrabajoRepositoryImpl
    private lateinit var dao: TrabajoDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = TrabajoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda trabajo correctamente`() = runTest {
        // Given
        val trabajo = Trabajo(
            trabajoId = 0,
            nombre = "Apartamento Familia Pérez",
            direccion = "Santo Domingo"
        )

        val trabajoSlot = slot<TrabajoEntity>()

        coEvery {
            dao.upsert(capture(trabajoSlot))
        } just runs

        // When
        repository.upsert(trabajo)

        // Then
        coVerify(exactly = 1) {
            dao.upsert(any())
        }

        assertEquals(
            trabajo.trabajoId,
            trabajoSlot.captured.trabajoId
        )
        assertEquals(
            trabajo.nombre,
            trabajoSlot.captured.nombre
        )
        assertEquals(
            trabajo.direccion,
            trabajoSlot.captured.direccion
        )
    }

    @Test
    fun `upsert actualiza trabajo correctamente`() = runTest {
        // Given
        val trabajo = Trabajo(
            trabajoId = 1,
            nombre = "Trabajo actualizado",
            direccion = "Santiago"
        )

        val trabajoSlot = slot<TrabajoEntity>()

        coEvery {
            dao.upsert(capture(trabajoSlot))
        } just runs

        // When
        repository.upsert(trabajo)

        // Then
        coVerify(exactly = 1) {
            dao.upsert(any())
        }

        assertEquals(
            1,
            trabajoSlot.captured.trabajoId
        )
        assertEquals(
            "Trabajo actualizado",
            trabajoSlot.captured.nombre
        )
    }

    @Test
    fun `delete elimina trabajo correctamente`() = runTest {
        // Given
        val trabajoId = 1

        coEvery {
            dao.delete(trabajoId)
        } just runs

        // When
        repository.delete(trabajoId)

        // Then
        coVerify(exactly = 1) {
            dao.delete(trabajoId)
        }
    }

    @Test
    fun `observeTrabajos retorna flow de trabajos`() = runTest {
        // Given
        val entities = listOf(
            TrabajoEntity(
                trabajoId = 1,
                nombre = "Trabajo 1",
                direccion = "Dirección 1"
            ),
            TrabajoEntity(
                trabajoId = 2,
                nombre = "Trabajo 2",
                direccion = null
            )
        )

        every {
            dao.observeAll()
        } returns flowOf(entities)

        // When
        val result = repository.observeTrabajos().first()

        // Then
        assertEquals(2, result.size)

        assertEquals(
            "Trabajo 1",
            result[0].nombre
        )
        assertEquals(
            "Dirección 1",
            result[0].direccion
        )
        assertEquals(
            "Trabajo 2",
            result[1].nombre
        )
        assertNull(result[1].direccion)
    }

    @Test
    fun `getById retorna trabajo por id`() = runTest {
        // Given
        val entity = TrabajoEntity(
            trabajoId = 1,
            nombre = "Trabajo de prueba",
            direccion = "La Vega"
        )

        coEvery {
            dao.getById(1)
        } returns entity

        // When
        val result = repository.getById(1)

        // Then
        assertNotNull(result)
        assertEquals(1, result?.trabajoId)
        assertEquals(
            "Trabajo de prueba",
            result?.nombre
        )
        assertEquals(
            "La Vega",
            result?.direccion
        )
    }

    @Test
    fun `getById retorna null cuando trabajo no existe`() = runTest {
        // Given
        coEvery {
            dao.getById(99)
        } returns null

        // When
        val result = repository.getById(99)

        // Then
        assertNull(result)
    }
}