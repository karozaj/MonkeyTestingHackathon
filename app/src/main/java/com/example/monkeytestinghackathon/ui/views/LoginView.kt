package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import com.example.monkeytestinghackathon.viewmodels.LoginViewModel

@Preview
@Composable
fun LoginView(
    onButtonClick: () -> Unit = { },
    viewModel: LoginViewModel = koinViewModel()
){
    Scaffold() { paddingValues ->
    Column(
        modifier = Modifier.padding(paddingValues)
    ){
        TextField("test", {})
        TextField("testt", {})

        //Text("Login view")
        Button( onClick = {onButtonClick()}) {Text("test navigation")}
    }
        }
}