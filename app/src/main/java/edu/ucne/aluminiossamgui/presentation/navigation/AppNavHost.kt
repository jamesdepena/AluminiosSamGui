package edu.ucne.aluminiossamgui.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import edu.ucne.aluminiossamgui.presentation.auth.AuthScreen
import edu.ucne.aluminiossamgui.presentation.auth.AuthUiEvent
import edu.ucne.aluminiossamgui.presentation.auth.AuthViewModel
import edu.ucne.aluminiossamgui.presentation.trabajo.edit.EditTrabajoScreen
import edu.ucne.aluminiossamgui.presentation.trabajo.list.ListTrabajoScreen
import edu.ucne.aluminiossamgui.presentation.hueco.edit.EditHuecoScreen
import edu.ucne.aluminiossamgui.presentation.hueco.list.ListHuecoScreen

@Composable
fun AppNavHost(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    when {
        authState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        authState.user == null -> {
            AuthScreen(
                state = authState,
                onEvent = authViewModel::onEvent
            )
        }

        else -> {
            val backStack = rememberNavBackStack(Screen.TrabajoList)

            NavDisplay(
                backStack = backStack,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Screen.TrabajoList> {
                        ListTrabajoScreen(
                            createTrabajo = { backStack.add(Screen.TrabajoEdit(id = null)) },
                            goToTrabajo = { trabajoId -> backStack.add(Screen.TrabajoEdit(id = trabajoId)) },
                            goToHuecos = { trabajoId -> backStack.add(Screen.HuecoList(trabajoId = trabajoId)) },
                            onSignOut = { authViewModel.onEvent(AuthUiEvent.SignOut) }
                        )
                    }

                    entry<Screen.TrabajoEdit> { key ->
                        EditTrabajoScreen(
                            trabajoId = key.id,
                            onBack = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            }
                        )
                    }

                    entry<Screen.HuecoList> { key ->
                        ListHuecoScreen(
                            trabajoId = key.trabajoId,
                            onBack = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            },
                            createHueco = { trabajoId ->
                                backStack.add(Screen.HuecoEdit(trabajoId = trabajoId, id = 0))
                            },
                            goToHueco = { trabajoId, huecoId ->
                                backStack.add(Screen.HuecoEdit(trabajoId = trabajoId, id = huecoId))
                            },
                            editTrabajo = { trabajoId ->
                                backStack.add(Screen.TrabajoEdit(id = trabajoId))
                            }
                        )
                    }

                    entry<Screen.HuecoEdit> { key ->
                        EditHuecoScreen(
                            trabajoId = key.trabajoId,
                            huecoId = key.id,
                            onBack = {
                                if (backStack.size > 1) {
                                    backStack.removeAt(backStack.size - 1)
                                }
                            }
                        )
                    }
                }
            )
        }
    }
}