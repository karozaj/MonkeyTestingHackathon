package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.R
import com.example.monkeytestinghackathon.ui.components.EventRow

@Composable
fun EventListView(
    onAddEventButtonClicked: () -> Unit = { },
    onEventClicked: (eventId: Int) -> Unit = { }
){
    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)){

            Box(
                Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    Modifier.fillMaxWidth()
                ) {
                    items(50) {
                        EventRow(
                            index = 1,
                            "Test",
                            "test",
                            "Krakow",
                            "2025-11-29",
                            5,
                            6,
                            onClick = {onEventClicked(it)}
                        )
                    }

                }

                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd){
                    // add event button
                    Button(onClick = {
                        onAddEventButtonClicked()
                    },
                        modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.Add_event))
                    }
                }
            }

        }
    }

}