package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.viewmodels.EventDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailView(
    eventId: String,
    viewModel: EventDetailViewModel = koinViewModel()
) {
    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Box(Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center) {
                Card(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text("Title: ")
                        Text("Description: ")
                        Text("Star date: ")
                        Text("End date:")
                    }
                }
            }

        }
    }
}