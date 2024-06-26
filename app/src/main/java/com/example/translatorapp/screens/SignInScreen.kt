package com.example.translatorapp.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.translatorapp.navigation.Screens

@Composable
fun SignInScreen(
    navController: NavController
) {
    val viewModel: SignInViewModel = viewModel()
    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    when(state.value) {
        is SignInState.Idle -> SignInContent(
            viewModel = viewModel,
            navController = navController,
        )
        is SignInState.Success -> TranslatorScreen(
            navController = navController,
        )
        is SignInState.Error -> Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun SignInContent(
    viewModel: SignInViewModel,
    navController: NavController,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sign In Screen",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (viewModel.checkIsInputValid(email,password)) {
                        viewModel.signIn(email, password)
                    } else {
                        Toast.makeText(
                            context,
                            "Empty Fields Are not Allowed !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.width(16.dp))

        }
        TextButton(onClick = { navController.navigate(Screens.SignUpScreen.route) }) {
            Text(
                "Not signed up yet? Click here",
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.align(Alignment.CenterVertically),
            )
        }

    }
}