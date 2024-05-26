package com.example.translatorapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp.repository.FirebaseAuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainViewModel(
) : ViewModel() {
    private val authenticationRepository = FirebaseAuthenticationRepository(FirebaseAuth.getInstance())

    var name = mutableStateOf("")

    init {
        viewModelScope.launch {
            var email = authenticationRepository.firebaseInstance.currentUser?.email
            email = email?.substringBefore("@")
            if (email != null) {
                name.value = email
            }

        }
    }
    fun signOut() {
        authenticationRepository.signOut()
    }
}