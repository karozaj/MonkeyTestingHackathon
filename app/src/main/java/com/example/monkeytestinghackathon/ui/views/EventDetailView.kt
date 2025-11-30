package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.monkeytestinghackathon.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monkeytestinghackathon.models.OneEvent
import com.example.monkeytestinghackathon.viewmodels.EventDetailViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailView(
    eventId: String,
    userId: String,
    viewModel: EventDetailViewModel = koinViewModel(),
    onBackButtonPressed: (String) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.updateEvent(
            viewModel.getEvent(eventId) ?: OneEvent(
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
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detale wydarzenia") },
                navigationIcon = {
                    IconButton(onClick = { onBackButtonPressed(userId) }) {
                        Icon(painterResource(R.drawable.arrow_left), contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    DetailText("Tytuł", state.event.title)
                    DetailText("Opis", state.event.description)
                    DetailText("Gra", state.event.game_type)
                    DetailText("Typ", state.event.category)
                    DetailText("Lokalizacja", state.event.location)
                    DetailText("Start", state.event.start_time)
                    DetailText("Koniec", state.event.end_time ?: "—")
                    DetailText(
                        "Participants",
                        "${state.event.participants_count} / ${state.event.max_participants ?: "-"}"
                    )

                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { viewModel.joinEventAsync(eventId, userId) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Dołącz")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = { viewModel.leaveEventAsync(eventId, userId) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        ) {
                            Text("Wyjdź")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailText(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
