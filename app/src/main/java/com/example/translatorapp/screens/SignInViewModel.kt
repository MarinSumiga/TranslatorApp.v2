package com.example.translatorapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.translatorapp.repository.FirebaseAuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class SignInState {
    object Idle : SignInState()
    data class Error(val message: String) : SignInState()
    object Success : SignInState()
}

class SignInViewModel() : ViewModel() {

    private val authenticationRepository = FirebaseAuthenticationRepository(FirebaseAuth.getInstance())
    val state = MutableStateFlow<SignInState>(SignInState.Idle)

    fun signIn(email: String, password: String) {
        state.value = SignInState.Idle
        viewModelScope.launch {
            if(authenticationRepository.signIn(email,password)==Result.success(FirebaseAuth.getInstance().currentUser)){
                state.value = SignInState.Success
            }
            else{
                state.value = SignInState.Error("An error occurred")
            }

        }
    }
    fun checkIsInputValid(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }


}