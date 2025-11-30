package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monkeytestinghackathon.models.OneEvent
import com.example.monkeytestinghackathon.viewmodels.EventDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventDetailView(
    eventId: String,
    userId: String,
    viewModel: EventDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit){
        viewModel.updateEvent(
        viewModel.getEvent(eventId)?: OneEvent(
            title = "",
            description = "",
            category = "",
            location = "",
            game_type = "",
            start_time = "",
            end_time = null,
            max_participants = null,
            id = "",
            participants_count = 0,
            created_at = ""
        ))
    }

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
                        Text("Title: ${state.event.title}")
                        DefaultSpacer()
                        Text("Description:  ${state.event.description}")
                        DefaultSpacer()
                        Text("Game:  ${state.event.game_type}")
                        DefaultSpacer()
                        Text("Type:  ${state.event.category}")
                        DefaultSpacer()
                        Text("Location:  ${state.event.location}")
                        DefaultSpacer()
                        Text("Start date:  ${state.event.start_time}")
                        DefaultSpacer()
                        Text("End date: ${state.event.end_time}")
                        DefaultSpacer()
                        Text("Participants:  ${state.event.participants_count}/ ${state.event.max_participants}")
                        DefaultSpacer()
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Button(onClick = {viewModel.joinEventAsync(eventId,userId)}) { Text("Join event") }
                            Button(onClick = {viewModel.leaveEventAsync(eventId,userId)}) { Text("Leave event") }
                        }


                    }
                }
            }

        }
    }
}