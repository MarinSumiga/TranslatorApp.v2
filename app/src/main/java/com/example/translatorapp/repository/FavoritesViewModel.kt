package com.example.translatorapp.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class FavoritesState {
    object Loading : FavoritesState()
    data class Success(val state: FavoriteTranslations?) : FavoritesState()
}

class FavoritesViewModel(
    private val repository: FavoritesRepository = FavoritesRepository()
) : ViewModel() {
    init {
        viewModelScope.launch {
            getFavorites()
        }
    }

    val state = MutableStateFlow<FavoritesState>(FavoritesState.Loading)

    private suspend fun getFavorites() {
        val result = repository.getFavorites()
        state.value = FavoritesState.Success(result)
    }
}