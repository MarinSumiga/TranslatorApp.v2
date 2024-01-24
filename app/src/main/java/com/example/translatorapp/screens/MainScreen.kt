@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.translatorapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
 import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.translatorapp.DE
import com.example.translatorapp.EN
import com.example.translatorapp.ES
import com.example.translatorapp.FR
import com.example.translatorapp.HR
import com.example.translatorapp.R
import com.example.translatorapp.navigation.Screens
import com.example.translatorapp.SpeechToText
import com.example.translatorapp.TranslatorClass
import com.example.translatorapp.outputText
import com.example.translatorapp.repository.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

var speechLanguage =  "HR"
var baseLanguage =  HR
var secondLanguage = EN
var x = ""
val textInput =  mutableStateOf("")

@Composable
fun TranslatorScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val database = FavoritesRepository()


    Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
    ){

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            LogoutUser(navController)
            Spacer(modifier = Modifier.width(16.dp))
            MenuButton(navController)
        }

        ScreenTitle("Translator")

        Row {
            Spacer(modifier = Modifier.width(16.dp))
            DropDownBaseSelector()
            Spacer(modifier = Modifier.width(30.dp))
            DropDownTargetSelector()
            Spacer(modifier = Modifier.width(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row{
            x = getTextInput()
            ButtonForSpeechToText(
                speech = SpeechToText(
                    speechLanguage,
                    context,
                    onTextRecognized = { results -> textInput.value = results })
            )
        }

        ButtonForTranslating(
            translation = TranslatorClass(), x
        )

        TranslatedTextBox(database,context)
    }
}


@Composable
fun ScreenTitle(
    title: String
) {
    Box(
        modifier = Modifier
            .padding(all = 14.dp)
            .background(color = Color.White)
            .fillMaxWidth()
    ){
        Text(
            text = title,
            style = TextStyle(color = Color.Black, fontSize = 26.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 16.dp)
                .align(Alignment.Center)

        )
    }
}

@Composable
fun getTextInput(
): String{
    TextField(
        value = textInput.value, onValueChange = { textInput.value = it},
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .width(250.dp),
        label = { Text("Enter your text here:")},
    )
    return textInput.value
}


@Composable
fun TranslatedTextBox(
    database: FavoritesRepository,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .padding(all = 16.dp)
            .background(color = Color.White)
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = MaterialTheme.shapes.medium
            )
            .height(300.dp)
            .fillMaxWidth()


    ) {
        Text(
            text = "Translated Text",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            style = TextStyle(color = Color.DarkGray, fontSize = 24.sp),
            textDecoration = TextDecoration.Underline
        )
        Text(
            outputText.value,
            modifier = Modifier.padding(top = 48.dp, start = 8.dp,end = 8.dp),
            style = TextStyle(color = Color.Black, fontSize = 20.sp)
        )
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    database.saveTextToFirestore(outputText.value,context)
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd),
        ) {
            Icon(
                Icons.Default.Star,
                contentDescription = "Star",
                tint = Color.Blue,
            )
        }
    }

}
@Composable
fun LogoutUser(
    navController: NavController
){
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var email = firebaseAuth.currentUser?.email // Get the current user's email from FirebaseAuth.
    email = email?.substringBefore("@")
    Row {
        Text("Hello, $email!", fontSize = 20.sp, style = TextStyle(Color.Black))
    }
    Button(
        onClick = {
            firebaseAuth.signOut()
            navController.navigate(Screens.SignInScreen.route)
            navController.popBackStack(Screens.MainScreen.route, true) // Pop back to the previous screen in the nav Controller
        }
    ) {
        Text("SignOut")
    }

}
@Composable
fun MenuButton(
    navController: NavController
) {
    IconButton(
        onClick = {
            navController.navigate(Screens.FavoritesScreen.route)
        }
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black
        )
    }
}


@Composable
fun ButtonForTranslating(translation: TranslatorClass, text:String) {
    val context = LocalContext.current
    Button(
        onClick = {
                translation.onTranslateButtonClick(text,context)
        },
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
    ) {
        Text(text = "Translate", style = TextStyle(fontSize = 20.sp))
    }
}
@Composable
fun ButtonForSpeechToText(
    speech: SpeechToText,
) {
    val isListening by remember { mutableStateOf(false) }
    Box (
        modifier = Modifier
           .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ){
        Button(
            onClick = {
                if(!isListening) {
                    speech.startListening()
                }
            },
            modifier = Modifier
                .padding()
                .size(70.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Microphone",
                modifier = Modifier.size(70.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownBaseSelector(
){
    val isExpanded = remember {
        mutableStateOf(false)
    }
    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = {isExpanded.value = it}
    ) {
        TextField(
            label = {
                Text(text = "Choose base language")
            },
            maxLines = 2,
            value =  baseLanguage,
            onValueChange ={},
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .width(150.dp)
        )
        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        ) {
                DropdownMenuItem(
                    text = { Text(text = "HR") },
                    onClick = {
                        baseLanguage = HR
                        speechLanguage =  "HR"
                        isExpanded.value = false
                    }
                )
            DropdownMenuItem(
                text = { Text(text = "EN") },
                onClick = {
                    baseLanguage = EN
                    speechLanguage =  "EN"
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "DE") },
                onClick = {
                    baseLanguage = DE
                    speechLanguage =  "DE"
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "ES") },
                onClick = {
                    baseLanguage = ES
                    speechLanguage =  "ES"
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "FR") },
                onClick = {
                    baseLanguage = FR
                    speechLanguage =  "FR"
                    isExpanded.value = false
                }
            )
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownTargetSelector(){

    val isExpanded = remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded.value,
        onExpandedChange = {isExpanded.value = it}
    ) {
        TextField(
            label = {
                Text(text = "Choose target language")

            },
            maxLines = 2,
            value =  secondLanguage,
            onValueChange ={},
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .menuAnchor()
                .width(150.dp)
        )

        ExposedDropdownMenu(
            expanded = isExpanded.value,
            onDismissRequest = { isExpanded.value = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "HR") },
                onClick = {
                    secondLanguage = HR
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "EN") },
                onClick = {
                    secondLanguage = EN
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "DE") },
                onClick = {
                    secondLanguage = DE
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "ES") },
                onClick = {
                    secondLanguage = ES
                    isExpanded.value = false
                }
            )
            DropdownMenuItem(
                text = { Text(text = "FR") },
                onClick = {
                    secondLanguage = FR
                    isExpanded.value = false
                }
            )
        }
    }
}


