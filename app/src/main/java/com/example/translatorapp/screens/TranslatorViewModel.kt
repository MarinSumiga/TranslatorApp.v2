package com.example.translatorapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.translatorapp.EN
import com.example.translatorapp.HR
import com.example.translatorapp.TranslatorClass

class TranslatorViewModel : ViewModel() {

    // State Management
    private val textInput = mutableStateOf("")
    private val outputText = mutableStateOf("")
    private val _isLoading = mutableStateOf(false)

    private val _isListening = mutableStateOf(false)

    private val baseLanguage = mutableStateOf(HR)
    private val secondLanguage = mutableStateOf(EN)

    private val translator = TranslatorClass()



}