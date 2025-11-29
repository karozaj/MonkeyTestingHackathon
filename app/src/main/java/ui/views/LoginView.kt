package ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun LoginView(
    onButtonClick: () -> Unit = { }
){
    Column(
    ){
        Text("Login view")
        Button( onClick = {onButtonClick()}) {Text("test navigation")}
    }
}