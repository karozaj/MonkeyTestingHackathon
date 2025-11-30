package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.R
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import com.example.monkeytestinghackathon.ui.components.EventRow
import com.example.monkeytestinghackathon.viewmodels.EventListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListView(
    onAddEventButtonClicked: () -> Unit = { },
    onEventClicked: (eventId: String, userId: String) -> Unit = { _, _ -> },
    viewModel: EventListViewModel = koinViewModel(),
    userId: String
) {

    LaunchedEffect(Unit) {
        viewModel.getEvents(userId)
    }

    val events = viewModel.events.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()

    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedEventType by remember { mutableStateOf<EventType?>(null) }

    var locationExpanded by remember { mutableStateOf(false) }
    var eventTypeExpanded by remember { mutableStateOf(false) }
    var popupExpanded by remember { mutableStateOf(false) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    "Wybierz wydarzenie!",
                    style = MaterialTheme.typography.headlineSmall
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    IconButton(onClick = { popupExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options")
                    }

                    DropdownMenu(
                        expanded = popupExpanded,
                        onDismissRequest = { popupExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Odśwież") },
                            onClick = {
                                popupExpanded = false
                                viewModel.getEventsFeed(userId)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Wyczyść filtry") },
                            onClick = {
                                popupExpanded = false
                                selectedLocation = null
                                selectedEventType = null
                                viewModel.getEventsFeed(userId)
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = locationExpanded,
                    onExpandedChange = { locationExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedLocation?.value ?: "Lokalizacja",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Location") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = locationExpanded,
                        onDismissRequest = { locationExpanded = false }
                    ) {
                        Location.entries.forEach { loc ->
                            DropdownMenuItem(
                                text = { Text(loc.value) },
                                onClick = {
                                    selectedLocation = loc
                                    locationExpanded = false
                                    viewModel.getEventsByLocalization(loc.value)
                                }
                            )
                        }
                    }
                }
                ExposedDropdownMenuBox(
                    expanded = eventTypeExpanded,
                    onExpandedChange = { eventTypeExpanded = it },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = selectedEventType?.value ?: "Typ wydarzenia",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Typ wydarzenia") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventTypeExpanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = eventTypeExpanded,
                        onDismissRequest = { eventTypeExpanded = false }
                    ) {
                        EventType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.value) },
                                onClick = {
                                    selectedEventType = type
                                    eventTypeExpanded = false
                                    viewModel.getEventsByCategory(type.key)
                                }
                            )
                        }
                    }
                }
            }
            Box(Modifier.fillMaxSize()) {
                if (isLoading.value) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
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
                                    onEventClicked(event.id, userId)
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
    }
}
