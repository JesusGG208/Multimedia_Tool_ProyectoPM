package com.example.multimedia_tool_proyectopm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.multimedia_tool_proyectopm.interfaces.AppNav
import com.example.multimedia_tool_proyectopm.ui.theme.Multimedia_Tool_ProyectoPMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Multimedia_Tool_ProyectoPMTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNav(navController)
                }
            }
        }
    }
}
