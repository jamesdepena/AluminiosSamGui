package edu.ucne.aluminiossamgui.data.repository

import android.net.Uri
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    @Before
    fun setup() {
        auth = mockk(relaxed = true)
        credentialManager = mockk(relaxed = true)

        repository = AuthRepositoryImpl(
            auth = auth,
            credentialManager = credentialManager
        )
    }

    @Test
    fun `getCurrentUser retorna usuario autenticado`() {
        // Given
        val firebaseUser = mockk<FirebaseUser>()
        val photoUrl = mockk<Uri>()

        every { auth.currentUser } returns firebaseUser
        every { firebaseUser.uid } returns "usuario-123"
        every { firebaseUser.displayName } returns "Juan Pérez"
        every { firebaseUser.email } returns "juan@example.com"
        every { firebaseUser.photoUrl } returns photoUrl
        every {
            photoUrl.toString()
        } returns "https://example.com/foto.jpg"

        // When
        val result = repository.getCurrentUser()

        // Then
        assertNotNull(result)
        assertEquals("usuario-123", result?.uid)
        assertEquals("Juan Pérez", result?.nombre)
        assertEquals("juan@example.com", result?.correo)
        assertEquals(
            "https://example.com/foto.jpg",
            result?.fotoUrl
        )

        verify(exactly = 1) {
            auth.currentUser
        }
    }

    @Test
    fun `getCurrentUser retorna null cuando no existe sesion`() {
        // Given
        every {
            auth.currentUser
        } returns null

        // When
        val result = repository.getCurrentUser()

        // Then
        assertNull(result)

        verify(exactly = 1) {
            auth.currentUser
        }
    }

    @Test
    fun `signOut cierra sesion correctamente`() = runTest {
        // Given
        every {
            auth.signOut()
        } just runs

        // When
        repository.signOut()

        // Then
        verify(exactly = 1) {
            auth.signOut()
        }
    }
}