package edu.ucne.aluminiossamgui.data.repository

import edu.ucne.aluminiossamgui.data.local.dao.HuecoDao
import edu.ucne.aluminiossamgui.data.local.entity.HuecoEntity
import edu.ucne.aluminiossamgui.domain.model.ColorMaterial
import edu.ucne.aluminiossamgui.domain.model.Hueco
import edu.ucne.aluminiossamgui.domain.model.TipoHueco
import edu.ucne.aluminiossamgui.domain.model.TipoMaterial
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class HuecoRepositoryImplTest {

    private lateinit var repository: HuecoRepositoryImpl
    private lateinit var dao: HuecoDao

    @Before
    fun setup() {
        dao = mockk(relaxed = true)
        repository = HuecoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda hueco correctamente`() = runTest {
        // Given
        val hueco = Hueco(
            huecoId = 0,
            trabajoId = 1,
            etiqueta = "H1",
            tipo = TipoHueco.CORREDERA,
            anchoBase = 50.0,
            largoBase = 60.0,
            tipoMaterial = TipoMaterial.TRADICIONAL,
            esTresVias = false,
            color = ColorMaterial.NEGRO
        )

        val huecoSlot = slot<HuecoEntity>()

        coEvery {
            dao.upsert(capture(huecoSlot))
        } just runs

        // When
        repository.upsert(hueco)

        // Then
        coVerify(exactly = 1) {
            dao.upsert(any())
        }

        assertEquals(
            hueco.huecoId,
            huecoSlot.captured.huecoId
        )
        assertEquals(
            hueco.trabajoId,
            huecoSlot.captured.trabajoId
        )
        assertEquals(
            "H1",
            huecoSlot.captured.etiqueta
        )
        assertEquals(
            "CORREDERA",
            huecoSlot.captured.tipo
        )
        assertEquals(
            "TRADICIONAL",
            huecoSlot.captured.tipoMaterial
        )
        assertEquals(
            "NEGRO",
            huecoSlot.captured.color
        )
        assertFalse(huecoSlot.captured.esTresVias)
    }

    @Test
    fun `upsert actualiza hueco correctamente`() = runTest {
        // Given
        val hueco = Hueco(
            huecoId = 2,
            trabajoId = 1,
            etiqueta = "H2 actualizado",
            tipo = TipoHueco.CRISTAL_FIJO,
            anchoBase = 40.0,
            largoBase = 50.0,
            tipoMaterial = TipoMaterial.P40,
            esTresVias = false,
            color = ColorMaterial.GRIS
        )

        val huecoSlot = slot<HuecoEntity>()

        coEvery {
            dao.upsert(capture(huecoSlot))
        } just runs

        // When
        repository.upsert(hueco)

        // Then
        coVerify(exactly = 1) {
            dao.upsert(any())
        }

        assertEquals(
            2,
            huecoSlot.captured.huecoId
        )
        assertEquals(
            "H2 actualizado",
            huecoSlot.captured.etiqueta
        )
        assertEquals(
            "CRISTAL_FIJO",
            huecoSlot.captured.tipo
        )
        assertEquals(
            "P40",
            huecoSlot.captured.tipoMaterial
        )
    }

    @Test
    fun `delete elimina hueco correctamente`() = runTest {
        // Given
        val huecoId = 1

        coEvery {
            dao.delete(huecoId)
        } just runs

        // When
        repository.delete(huecoId)

        // Then
        coVerify(exactly = 1) {
            dao.delete(huecoId)
        }
    }

    @Test
    fun `observeByTrabajoId retorna huecos del trabajo`() = runTest {
        // Given
        val entities = listOf(
            HuecoEntity(
                huecoId = 1,
                trabajoId = 10,
                etiqueta = "H1",
                tipo = "CORREDERA",
                anchoBase = 50.0,
                largoBase = 60.0,
                tipoMaterial = "P65",
                esTresVias = false,
                color = "NEGRO"
            ),
            HuecoEntity(
                huecoId = 2,
                trabajoId = 10,
                etiqueta = "H2",
                tipo = "CRISTAL_FIJO",
                anchoBase = 30.0,
                largoBase = 40.0,
                tipoMaterial = "P40",
                esTresVias = false,
                color = "BLANCO"
            )
        )

        every {
            dao.observeByTrabajoId(10)
        } returns flowOf(entities)

        // When
        val result =
            repository.observeByTrabajoId(10).first()

        // Then
        assertEquals(2, result.size)

        assertEquals(
            "H1",
            result[0].etiqueta
        )
        assertEquals(
            TipoHueco.CORREDERA,
            result[0].tipo
        )
        assertEquals(
            TipoMaterial.P65,
            result[0].tipoMaterial
        )

        assertEquals(
            "H2",
            result[1].etiqueta
        )
        assertEquals(
            TipoHueco.CRISTAL_FIJO,
            result[1].tipo
        )
        assertEquals(
            TipoMaterial.P40,
            result[1].tipoMaterial
        )
    }

    @Test
    fun `getById retorna hueco por id`() = runTest {
        // Given
        val entity = HuecoEntity(
            huecoId = 1,
            trabajoId = 10,
            etiqueta = "H1",
            tipo = "CORREDERA",
            anchoBase = 50.0,
            largoBase = 60.0,
            tipoMaterial = "P92",
            esTresVias = true,
            color = "CAOBA"
        )

        coEvery {
            dao.getById(1)
        } returns entity

        // When
        val result = repository.getById(1)

        // Then
        assertNotNull(result)
        assertEquals(1, result?.huecoId)
        assertEquals(10, result?.trabajoId)
        assertEquals("H1", result?.etiqueta)
        assertEquals(
            TipoHueco.CORREDERA,
            result?.tipo
        )
        assertEquals(
            TipoMaterial.P92,
            result?.tipoMaterial
        )
        assertEquals(
            ColorMaterial.CAOBA,
            result?.color
        )
        assertEquals(true, result?.esTresVias)
    }

    @Test
    fun `getById retorna null cuando hueco no existe`() = runTest {
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