// Kotlin
package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import com.example.monkeytestinghackathon.viewmodels.RegisterUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserView(
    viewModel: RegisterUserViewModel = RegisterUserViewModel(),
) {
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var selectedGames by remember { mutableStateOf(setOf<CardGameTypes>()) }
    var showGamesDialog by remember { mutableStateOf(false) }

    var locationExpanded by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<Location?>(null) }

    var prefExpanded by remember { mutableStateOf(false) }
    var selectedPreference by remember { mutableStateOf<EventType?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Tell us something about you!") }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Card games (multi-select)")
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { showGamesDialog = true }) {
                val picked = if (selectedGames.isEmpty()) "Select games" else selectedGames.joinToString { it.value }
                Text(picked)
            }

            if (showGamesDialog) {
                GamesMultiSelectDialog(
                    allGames = CardGameTypes.entries,
                    initialSelection = selectedGames,
                    onConfirm = { newSelection ->
                        selectedGames = newSelection
                        showGamesDialog = false
                    },
                    onDismiss = { showGamesDialog = false }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Localization")
            ExposedDropdownMenuBox(
                expanded = locationExpanded,
                onExpandedChange = { locationExpanded = !locationExpanded }
            ) {
                TextField(
                    value = selectedLocation?.name ?: "",
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
                            text = { Text(loc.name) },
                            onClick = {
                                selectedLocation = loc
                                locationExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Preferred event types")
            ExposedDropdownMenuBox(
                expanded = prefExpanded,
                onExpandedChange = { prefExpanded = !prefExpanded }
            ) {
                TextField(
                    value = selectedPreference?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select preferred event type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = prefExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = prefExpanded,
                    onDismissRequest = { prefExpanded = false }
                ) {
                    EventType.entries.forEach { pref ->
                        DropdownMenuItem(
                            text = { Text(pref.name) },
                            onClick = {
                                selectedPreference = pref
                                prefExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // viewModel.register(username, description, selectedGames, selectedLocation, selectedPreference)
            }) {
                Text("Register")
            }
        }
    }
}

@Composable
private fun GamesMultiSelectDialog(
    allGames: List<CardGameTypes>,
    initialSelection: Set<CardGameTypes>,
    onConfirm: (Set<CardGameTypes>) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelection by remember { mutableStateOf(initialSelection) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Wybierz gry") },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
            ) {
                items(allGames) { game ->
                    val checked = game in tempSelection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                tempSelection = if (checked) tempSelection - game else tempSelection + game
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(game.value)
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                tempSelection = if (it) tempSelection + game else tempSelection - game
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelection) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
