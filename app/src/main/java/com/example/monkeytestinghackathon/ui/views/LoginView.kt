package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.example.monkeytestinghackathon.viewmodels.LoginViewModel

@Composable
fun LoginView(
    onGoogleSignInClick: () -> Unit = { },
    viewModel: LoginViewModel = koinViewModel()
) {

    Scaffold { paddingValues ->
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { onGoogleSignInClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign In via Google")
            }
    }
}
