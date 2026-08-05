package edu.ucne.aluminiossamgui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.aluminiossamgui.presentation.navigation.AppNavHost
import edu.ucne.aluminiossamgui.ui.theme.AluminiosSamGuiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AluminiosSamGuiTheme {
                AppNavHost()
            }
        }
    }
}