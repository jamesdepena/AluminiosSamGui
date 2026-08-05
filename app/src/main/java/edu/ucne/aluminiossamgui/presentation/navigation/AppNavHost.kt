package edu.ucne.aluminiossamgui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import edu.ucne.aluminiossamgui.presentation.casa.edit.EditCasaScreen
import edu.ucne.aluminiossamgui.presentation.casa.list.ListCasaScreen
import edu.ucne.aluminiossamgui.presentation.hueco.edit.EditHuecoScreen
import edu.ucne.aluminiossamgui.presentation.hueco.list.ListHuecoScreen

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(Screen.CasaList)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Screen.CasaList> {
                ListCasaScreen(
                    createCasa = { backStack.add(Screen.CasaEdit(id = null)) },
                    goToCasa = { casaId -> backStack.add(Screen.CasaEdit(id = casaId)) },
                    goToHuecos = { casaId -> backStack.add(Screen.HuecoList(casaId = casaId)) }
                )
            }

            entry<Screen.CasaEdit> { key ->
                EditCasaScreen(
                    casaId = key.id,
                    onBack = { if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    }
                )
            }

            entry<Screen.HuecoList> { key ->
                ListHuecoScreen(
                    casaId = key.casaId,
                    onBack = { if (backStack.size > 1) {
                            backStack.removeAt(backStack.size - 1)
                        }
                    },
                    createHueco = { casaId ->
                        backStack.add(Screen.HuecoEdit(casaId = casaId, id = 0))
                    },
                    goToHueco = { casaId, huecoId ->
                        backStack.add(Screen.HuecoEdit(casaId = casaId, id = huecoId))
                    },
                    editCasa = { casaId ->
                        backStack.add(Screen.CasaEdit(id = casaId))
                    }
                )
            }

            entry<Screen.HuecoEdit> { key ->
                EditHuecoScreen(
                    casaId = key.casaId,
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