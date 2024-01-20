package com.example.translatorapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.translatorapp.screens.FavoritesScreen
import com.example.translatorapp.screens.SignInScreen
import com.example.translatorapp.screens.SignUpScreen
import com.example.translatorapp.screens.TranslatorScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    NavHost(navController =navController , startDestination = Screens.SignInScreen.route){
        
        composable(route = Screens.MainScreen.route){
            TranslatorScreen(navController = navController)
        }

        composable(route = Screens.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }

        composable(route = Screens.SignInScreen.route){
            
            if (firebaseAuth.currentUser != null) {
                TranslatorScreen(navController = navController)
            }
            else {
                SignInScreen(navController = navController)
            }
        }

        composable(route = Screens.FavoritesScreen.route){
            FavoritesScreen(navController = navController)
        }

    }
}