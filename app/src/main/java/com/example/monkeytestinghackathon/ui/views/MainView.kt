package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainView(
    onButtonClick: () -> Unit = { }
){
    Scaffold() { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)){



            LazyColumn {

            }

        }
    }

    Column(
    ){
        Text("Main view")
        Button( onClick = {onButtonClick()}) {Text("test navigation")}
    }
}