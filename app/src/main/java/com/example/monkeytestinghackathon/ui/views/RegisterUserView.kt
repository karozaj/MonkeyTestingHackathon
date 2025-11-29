package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import com.example.monkeytestinghackathon.ui.components.MultiSelectDialog
import com.example.monkeytestinghackathon.viewmodels.RegisterUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserView(
    modifier: Modifier = Modifier,
    viewModel: RegisterUserViewModel = RegisterUserViewModel(),
    userId: String
) {
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedGames by remember { mutableStateOf(setOf<CardGameTypes>()) }
    var showGamesDialog by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }
    var selectedEventTypes by remember { mutableStateOf(setOf<EventType>()) }
    var showEventTypesDialog by remember { mutableStateOf(false) }
    var registering by remember { mutableStateOf(false) }
    var successState by remember { mutableStateOf<Boolean?>(null) }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                modifier = modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text("Tell us something about you!") },
                maxLines = Int.MAX_VALUE
            )
            Spacer(Modifier.height(12.dp))
            Text("Card games (multi-select)")
            Spacer(Modifier.height(8.dp))
            Button(onClick = { showGamesDialog = true }) {
                val picked = if (selectedGames.isEmpty()) "Select games" else selectedGames.joinToString { it.value }
                Text(picked)
            }
            if (showGamesDialog) {
                MultiSelectDialog(
                    title = "Select Card Games",
                    allItems = CardGameTypes.entries.toList(),
                    initialSelection = selectedGames,
                    label = { it.value },
                    onConfirm = {
                        selectedGames = it
                        showGamesDialog = false
                    },
                    onDismiss = { showGamesDialog = false }
                )
            }
            Spacer(Modifier.height(12.dp))
            Text("Localization")
            ExposedDropdownMenuBox(
                expanded = locationExpanded,
                onExpandedChange = { locationExpanded = !locationExpanded }
            ) {
                TextField(
                    value = selectedLocation?.value ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select your nearest city") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded) },
                    modifier = Modifier.menuAnchor()
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
                            }
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Preferred event types")
            Button(onClick = { showEventTypesDialog = true }) {
                val picked = if (selectedEventTypes.isEmpty()) "Select event types" else selectedEventTypes.joinToString { it.value }
                Text(picked)
            }
            if (showEventTypesDialog) {
                MultiSelectDialog(
                    title = "Select Event Types",
                    allItems = EventType.entries.toList(),
                    initialSelection = selectedEventTypes,
                    label = { it.value },
                    onConfirm = {
                        selectedEventTypes = it
                        showEventTypesDialog = false
                    },
                    onDismiss = { showEventTypesDialog = false }
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(
                enabled = !registering && username.isNotBlank() && selectedLocation != null,
                onClick = {
                    registering = true
                    viewModel.createUserAccount(
                        user_id = userId,
                        username = username,
                        location = selectedLocation?.value ?: "",
                        description = description,
                        preferred_categories = selectedEventTypes.map { it.value },
                        preferred_game_types = selectedGames.map { it.value },
                        onComplete = {
                            successState = it
                            registering = false
                        }
                    )
                }
            ) {
                Text(if (registering) "Registering..." else "Register")
            }
            successState?.let {
                Spacer(Modifier.height(12.dp))
                Text(if (it) "Registration successful" else "Registration failed", color = if (it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
            }
        }
    }
}
