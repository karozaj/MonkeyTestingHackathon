package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    onRegistrationComplete: () -> Unit = {},
    onSkip: () -> Unit = {},
    viewModel: RegisterUserViewModel = RegisterUserViewModel(),
    userId: String
) {
    var username by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var selectedGames by rememberSaveable { mutableStateOf(setOf<CardGameTypes>()) }
    var showGamesDialog by remember { mutableStateOf(false) }

    var locationExpanded by remember { mutableStateOf(false) }
    var selectedLocation by rememberSaveable { mutableStateOf<Location?>(null) }

    var selectedEventTypes by rememberSaveable { mutableStateOf(setOf<EventType>()) }
    var showEventTypesDialog by remember { mutableStateOf(false) }

    var registering by remember { mutableStateOf(false) }
    var successState by remember { mutableStateOf<Boolean?>(null) }

    Scaffold { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Text(
                "Create your profile",
                style = MaterialTheme.typography.headlineSmall
            )

            // Username
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )

            // Description
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = { description = it },
                label = { Text("Tell us something about you") },
                minLines = 4,
            )

            // --- GAMES SELECT ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Card games", style = MaterialTheme.typography.labelLarge)

                Button(
                    onClick = { showGamesDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (selectedGames.isEmpty())
                            "Select games"
                        else selectedGames.joinToString { it.value }
                    )
                }
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

            // --- LOCATION ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Location", style = MaterialTheme.typography.labelLarge)

                ExposedDropdownMenuBox(
                    expanded = locationExpanded,
                    onExpandedChange = { locationExpanded = !locationExpanded }
                ) {
                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        value = selectedLocation?.value ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nearest city") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationExpanded)
                        }
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
            }

            // --- EVENT TYPES ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Preferred event types", style = MaterialTheme.typography.labelLarge)

                Button(
                    onClick = { showEventTypesDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (selectedEventTypes.isEmpty())
                            "Select event types"
                        else selectedEventTypes.joinToString { it.value }
                    )
                }
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

            // --- REGISTER BUTTON ---
            Button(
                onClick = {
                    registering = true
                    viewModel.createUserAccount(
                        user_id = userId,
                        username = username,
                        location = selectedLocation?.value.orEmpty(),
                        description = description,
                        preferred_categories = selectedEventTypes.map { it.value },
                        preferred_game_types = selectedGames.map { it.value },
                        onComplete = {
                            successState = it
                            registering = false
                            onRegistrationComplete()
                        }
                    )
                },
                enabled = !registering && username.isNotBlank() && selectedLocation != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (registering) "Registering…" else "Create account")
            }

            // --- SUCCESS MESSAGE ---
            successState?.let { success ->
                Text(
                    text = if (success) "Registration successful!" else "Registration failed.",
                    color = if (success) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                )
            }

            // Skip
            TextButton(onClick = onSkip) {
                Text("Back to login")
            }
        }
    }
}

