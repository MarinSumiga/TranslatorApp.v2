@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.translatorapp.screens

import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
import com.example.translatorapp.SpeechToText
import com.example.translatorapp.TranslatorClass
import com.example.translatorapp.navigation.Screens
import com.example.translatorapp.outputText
import com.example.translatorapp.repository.FavoritesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

var speechLanguage =  "HR"
var baseLanguage = mutableStateOf(HR)
var secondLanguage  = mutableStateOf(EN)
var x = ""
val textInput =  mutableStateOf("")

@Composable
fun TranslatorScreen(navController: NavController) {
    val context = LocalContext.current
    val database = FavoritesRepository()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationMenu(navController, scope, drawerState)
        },
        gesturesEnabled = true,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    MenuButton(scope, drawerState) // Open drawer on button click
                }

                ScreenTitle("Translator")

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    LanguageDropdownMenu(
                        selectedLanguage = baseLanguage.value,
                        onLanguageSelected = { newLanguage ->
                            baseLanguage.value = newLanguage
                            speechLanguage = newLanguage
                        },
                        languages = listOf(HR, EN, DE, ES, FR),
                        label = "Base"
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    LanguageDropdownMenu(
                        selectedLanguage = secondLanguage.value,
                        onLanguageSelected = { secondLanguage.value = it },
                        languages = listOf(HR, EN, DE, ES, FR),
                        label = "Target"
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    x = getTextInput()
                    ButtonForSpeechToText(
                        speech = SpeechToText(
                            speechLanguage,
                            context,
                            onTextRecognized = { results -> textInput.value = results }
                        )
                    )
                }

                ButtonForTranslating(
                    translation = TranslatorClass(), x
                )

                TranslatedTextBox(database, context)
            }
        }
    )
}

@Composable
fun MenuButton( scope: CoroutineScope, drawerState: DrawerState) {
    IconButton(
        onClick = { scope.launch { drawerState.open() } }
    ) {
        Icon(imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            tint = Color.Black
        )
    }
}

// Composable for Navigation Menu
@Composable
fun NavigationMenu(
    navController: NavController,
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var email = firebaseAuth.currentUser?.email
    email = email?.substringBefore("@")

    Column(
        modifier = Modifier.fillMaxWidth(0.7f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "User", tint = Color.Black)
                        Text("Dobrodošli, $email", style = MaterialTheme.typography.headlineSmall, color = Color.Black)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                // 'Favorites' navigation item
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorites") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screens.FavoritesScreen.route)
                        scope.launch { drawerState.close() } // Close the drawer
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(24.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))
                // 'Logout' navigation item
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") },
                    label = { Text("Logout") },
                    selected = false,
                    onClick = {
                        firebaseAuth.signOut()
                        navController.navigate(Screens.SignInScreen.route) {
                            popUpTo(Screens.MainScreen.route) { inclusive = true }
                        }
                        scope.launch { drawerState.close() } // Close the drawer
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Black, shape = RoundedCornerShape(24.dp))
                )
            }
        }
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
            .background(color = Color.White, shape = RoundedCornerShape(8.dp)) // Rounded corners
            .border(
                width = 1.dp,
                color = Color.LightGray, // Softer border color
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp)) // Subtle shadow
            .height(300.dp)
            .fillMaxWidth()

    ) {
        Text(
            text = "Translated Text",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            style = TextStyle(
                color = Color.DarkGray,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif
            ),
            textDecoration = TextDecoration.Underline
        )
        Text(
            outputText.value,
            modifier = Modifier.padding(top = 48.dp, start = 8.dp,end = 8.dp),
            style = TextStyle(color = Color.Black, fontSize = 20.sp)
        )
        Button(
            onClick = {
                if(outputText.value != "") {
                    coroutineScope.launch {
                        database.saveTextToFirestore(outputText.value,context)
                    }
                }
                else {
                    Toast.makeText(context, "Favorite cannot be empty!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
        ) {
            Icon(
                Icons.Default.FavoriteBorder,
                contentDescription = "Heart",
                tint = Color.Blue,
            )
        }
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
            .fillMaxWidth() ,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
    ) {
        Text(text = "Translate", style = TextStyle(fontSize = 20.sp), color = Color.White)
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
                .size(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            shape = RoundedCornerShape(percent = 50)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Microphone",
//                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdownMenu(
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit,
    languages: List<String>,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectedLanguage by remember { mutableStateOf(selectedLanguage) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(150.dp)
    ) {
        TextField(
            readOnly = true,
            value = currentSelectedLanguage,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    onClick = {
                        currentSelectedLanguage = language
                        onLanguageSelected(language) // Update the selected language
                        expanded = false
                    },
                    text = {language.let { Text(it) }}
                )
            }
        }
    }
}



