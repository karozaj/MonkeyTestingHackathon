package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.R
import com.example.monkeytestinghackathon.ui.components.EventRow
import com.example.monkeytestinghackathon.viewmodels.EventListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventListView(
    onAddEventButtonClicked: () -> Unit = { },
    onEventClicked: (eventId: String) -> Unit = { },
    viewModel: EventListViewModel = koinViewModel(),
    userId: String
){

    LaunchedEffect(Unit) {
        viewModel.getEvents(userId)
    }

    val events = viewModel.events.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
                    LazyColumn(
                        Modifier.fillMaxWidth()
                    ) {
                        items(events.value.size) { index ->
                            val event = events.value[index]
                            EventRow(
                                index = index,
                                title = event.title,
                                gameName = event.game_type,
                                location = event.location,
                                date = event.start_time,
                                currentParticipants = event.participants_count,
                                maxParticipants = event.max_participants ?: 1,
                                onClick = {
                                    onEventClicked(event.id)
                                }
                            )
                        }
                    }
                }

                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(
                        onClick = { onAddEventButtonClicked() },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(stringResource(R.string.Add_event))
                    }
                }
            }


}
