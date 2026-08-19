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
            nombreCliente = "Carlos Pérez",
            telefonoCliente = "809-555-1234",
            direccion = "Santo Domingo",
            notas = "Llamar antes de realizar la visita."
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
            trabajo.nombreCliente,
            trabajoSlot.captured.nombreCliente
        )
        assertEquals(
            trabajo.telefonoCliente,
            trabajoSlot.captured.telefonoCliente
        )
        assertEquals(
            trabajo.direccion,
            trabajoSlot.captured.direccion
        )
        assertEquals(
            trabajo.notas,
            trabajoSlot.captured.notas
        )
    }

    @Test
    fun `upsert actualiza trabajo correctamente`() = runTest {
        // Given
        val trabajo = Trabajo(
            trabajoId = 1,
            nombre = "Trabajo actualizado",
            nombreCliente = "María Rodríguez",
            telefonoCliente = "829-555-4567",
            direccion = "Santiago",
            notas = "Instalar durante la mañana."
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
        assertEquals(
            "María Rodríguez",
            trabajoSlot.captured.nombreCliente
        )
        assertEquals(
            "829-555-4567",
            trabajoSlot.captured.telefonoCliente
        )
        assertEquals(
            "Santiago",
            trabajoSlot.captured.direccion
        )
        assertEquals(
            "Instalar durante la mañana.",
            trabajoSlot.captured.notas
        )
    }

    @Test
    fun `upsert guarda campos opcionales nulos correctamente`() =
        runTest {
            // Given
            val trabajo = Trabajo(
                trabajoId = 0,
                nombre = "Trabajo sin datos opcionales",
                nombreCliente = null,
                telefonoCliente = null,
                direccion = null,
                notas = null
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
                "Trabajo sin datos opcionales",
                trabajoSlot.captured.nombre
            )
            assertNull(
                trabajoSlot.captured.nombreCliente
            )
            assertNull(
                trabajoSlot.captured.telefonoCliente
            )
            assertNull(
                trabajoSlot.captured.direccion
            )
            assertNull(
                trabajoSlot.captured.notas
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
                nombreCliente = "Carlos Pérez",
                telefonoCliente = "809-555-1234",
                direccion = "Dirección 1",
                notas = "Primera nota"
            ),
            TrabajoEntity(
                trabajoId = 2,
                nombre = "Trabajo 2",
                nombreCliente = null,
                telefonoCliente = null,
                direccion = null,
                notas = null
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
            1,
            result[0].trabajoId
        )
        assertEquals(
            "Trabajo 1",
            result[0].nombre
        )
        assertEquals(
            "Carlos Pérez",
            result[0].nombreCliente
        )
        assertEquals(
            "809-555-1234",
            result[0].telefonoCliente
        )
        assertEquals(
            "Dirección 1",
            result[0].direccion
        )
        assertEquals(
            "Primera nota",
            result[0].notas
        )

        assertEquals(
            2,
            result[1].trabajoId
        )
        assertEquals(
            "Trabajo 2",
            result[1].nombre
        )
        assertNull(result[1].nombreCliente)
        assertNull(result[1].telefonoCliente)
        assertNull(result[1].direccion)
        assertNull(result[1].notas)
    }

    @Test
    fun `getById retorna trabajo por id`() = runTest {
        // Given
        val entity = TrabajoEntity(
            trabajoId = 1,
            nombre = "Trabajo de prueba",
            nombreCliente = "Ana Gómez",
            telefonoCliente = "829-555-1234",
            direccion = "La Vega",
            notas = "Instalación en horario de la mañana."
        )

        coEvery {
            dao.getById(1)
        } returns entity

        // When
        val result = repository.getById(1)

        // Then
        assertNotNull(result)

        assertEquals(
            1,
            result?.trabajoId
        )
        assertEquals(
            "Trabajo de prueba",
            result?.nombre
        )
        assertEquals(
            "Ana Gómez",
            result?.nombreCliente
        )
        assertEquals(
            "829-555-1234",
            result?.telefonoCliente
        )
        assertEquals(
            "La Vega",
            result?.direccion
        )
        assertEquals(
            "Instalación en horario de la mañana.",
            result?.notas
        )
    }

    @Test
    fun `getById retorna trabajo con campos opcionales nulos`() =
        runTest {
            // Given
            val entity = TrabajoEntity(
                trabajoId = 2,
                nombre = "Trabajo básico",
                nombreCliente = null,
                telefonoCliente = null,
                direccion = null,
                notas = null
            )

            coEvery {
                dao.getById(2)
            } returns entity

            // When
            val result = repository.getById(2)

            // Then
            assertNotNull(result)
            assertEquals(2, result?.trabajoId)
            assertEquals("Trabajo básico", result?.nombre)
            assertNull(result?.nombreCliente)
            assertNull(result?.telefonoCliente)
            assertNull(result?.direccion)
            assertNull(result?.notas)
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