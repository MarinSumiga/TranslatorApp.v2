package com.example.translatorapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp.repository.FirebaseAuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val authenticationRepository = FirebaseAuthenticationRepository(FirebaseAuth.getInstance())

    fun signUp(email: String, password: String,  onSuccess: () -> Unit,onError: (String) -> Unit) {

        viewModelScope.launch {
            try {
                authenticationRepository.signUp(email, password)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "An error occurred")
            }
        }
    }

}