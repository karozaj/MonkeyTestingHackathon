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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.monkeytestinghackathon.R
import com.example.monkeytestinghackathon.ui.components.EventRow
import com.example.monkeytestinghackathon.viewmodels.EventListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EventListView(
    onAddEventButtonClicked: () -> Unit = { },
    onEventClicked: (eventId: Int) -> Unit = { },
    viewModel: EventListViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                    items(state.eventsList.size){
                        val event = state.eventsList[it]
                        EventRow(
                            index = it,
                            title = event.title,
                            gameName = event.gameType.value,
                            location = event.location.toString(),
                            date = event.startTime.toString(),
                            currentParticipants = event.participantsCount,
                            maxParticipants = event.maxParticipants,
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