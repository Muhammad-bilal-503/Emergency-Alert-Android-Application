package com.example.emergencyalertandroidapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.emergencyalertandroidapplication.ui.screen.auth.LoginScreen
import com.example.emergencyalertandroidapplication.ui.screen.auth.RegisterScreen
import com.example.emergencyalertandroidapplication.ui.screen.contacts.ContactsScreen
import com.example.emergencyalertandroidapplication.ui.screen.history.HistoryScreen
import com.example.emergencyalertandroidapplication.ui.screen.home.HomeScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Contacts : Screen("contacts")
    object History : Screen("history")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToContacts = { navController.navigate(Screen.Contacts.route) },
                onNavigateToHistory = { navController.navigate(Screen.History.route) }
            )
        }
        
        composable(Screen.Contacts.route) {
            ContactsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
