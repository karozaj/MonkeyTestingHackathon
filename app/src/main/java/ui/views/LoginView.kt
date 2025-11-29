package ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.androidx.compose.koinViewModel
import viewmodels.LoginViewModel

@Composable
fun LoginView(
    onButtonClick: () -> Unit = { },
    viewModel: LoginViewModel = koinViewModel()
){
    Column(
    ){
        Text("Login view")
        Button( onClick = {onButtonClick()}) {Text("test navigation")}
    }
}