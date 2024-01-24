package com.example.translatorapp.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.translatorapp.navigation.Screens
import com.example.translatorapp.repository.FavoriteTranslations
import com.example.translatorapp.repository.FavoritesState
import com.example.translatorapp.repository.FavoritesViewModel


@Composable
fun FavoritesScreen(
    navController: NavController
) {
    val viewModel:  FavoritesViewModel = viewModel()
    val state = viewModel.state.collectAsState()

    when(state.value) {
        is FavoritesState.Loading -> Loading()
        is FavoritesState.Success -> FavoritesContent(
            state = (state.value as FavoritesState.Success).state,
            navController = navController
        )
    }
}
@Composable
fun Loading(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesContent(
    state: FavoriteTranslations?,
    navController: NavController
) {

    val list = mutableListOf<String>()
    if (state != null) {
        list.addAll(state.FavoritedTranslations)
    }
    else{
        list.add("No translations favorited")
    }


    Surface(color = MaterialTheme.colorScheme.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        "Favorited translations",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                ) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screens.MainScreen.route)
                            navController.popBackStack(Screens.FavoritesScreen.route, true)

                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
                modifier = Modifier.align(Alignment.Start)
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(list) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Box(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
    

}
