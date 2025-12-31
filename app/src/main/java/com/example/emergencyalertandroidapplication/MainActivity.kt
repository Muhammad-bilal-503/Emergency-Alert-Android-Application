package com.example.emergencyalertandroidapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.emergencyalertandroidapplication.navigation.NavGraph
import com.example.emergencyalertandroidapplication.navigation.Screen
import com.example.emergencyalertandroidapplication.ui.theme.EmergencyAlertAndroidApplicationTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmergencyAlertAndroidApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmergencyAppNavigation()
                }
            }
        }
    }
}

@Composable
fun EmergencyAppNavigation() {
    val navController = rememberNavController()
    val app = androidx.compose.ui.platform.LocalContext.current.applicationContext as EmergencyApp
    
    // Determine start destination based on auth state
    val startDestination = if (app.authRepository.isUserLoggedIn()) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }
    
    NavGraph(
        navController = navController,
        startDestination = startDestination
    )
}