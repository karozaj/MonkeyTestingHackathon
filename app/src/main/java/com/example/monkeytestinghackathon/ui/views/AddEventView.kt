package com.example.monkeytestinghackathon.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventView (){

    var gameDropdownExpanded: Boolean by remember { mutableStateOf(true) }
    var eventTypeDropdownExpanded: Boolean by remember { mutableStateOf(false)}
    var locationDropdownExpanded: Boolean by remember { mutableStateOf(false)}

    fun expandDropdown(
        gameDropdown: Boolean = false,
        eventTypeDropdown: Boolean = false,
        locationDropdown: Boolean = false
    ){
        gameDropdownExpanded=gameDropdown
        eventTypeDropdownExpanded=eventTypeDropdown
        locationDropdownExpanded=locationDropdown
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
                    Column(Modifier.padding(8.dp)) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "Tytuł",
                            onValueChange = {},
                            label = { androidx.compose.material3.Text("Event Name") },
                        )
                        DefaultSpacer()
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "Opis",
                            onValueChange = {},
                            label = { androidx.compose.material3.Text("Event Name") },
                        )
                        DefaultSpacer()

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth()
                                .clickable(onClick = {expandDropdown(gameDropdown = true)}),
                            value = "Opis",
                            readOnly = true,
                            onValueChange = {},
                            label = { androidx.compose.material3.Text("Event Name") },
                        )

                        //game
                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = gameDropdownExpanded,
                            onExpandedChange = { expandDropdown(gameDropdown = !gameDropdownExpanded) }
                        ) {
                            TextField(

                                value = "test",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select your nearest city") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gameDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = gameDropdownExpanded,
                                onDismissRequest = { gameDropdownExpanded = false }
                            ) {
                                CardGameTypes.entries.forEach { game ->
                                    DropdownMenuItem(
                                        text = { Text(game.name) },
                                        onClick = {

                                        }
                                    )
                                }
                            }
                        }

                        //event type
                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = eventTypeDropdownExpanded,
                            onExpandedChange = { expandDropdown(eventTypeDropdown = !eventTypeDropdownExpanded) }
                        ) {
                            TextField(

                                value = "test",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select your nearest city") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventTypeDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = eventTypeDropdownExpanded,
                                onDismissRequest = { eventTypeDropdownExpanded = false }
                            ) {
                                EventType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.name) },
                                        onClick = {

                                        }
                                    )
                                }
                            }
                        }


                        DefaultSpacer()
                        //location
                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = locationDropdownExpanded,
                            onExpandedChange = { expandDropdown(locationDropdown = !locationDropdownExpanded) }
                        ) {
                            TextField(

                                value = "test",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select your nearest city") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = locationDropdownExpanded,
                                onDismissRequest = { locationDropdownExpanded = false }
                            ) {
                                Location.entries.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(location.name) },
                                        onClick = {

                                        }
                                    )
                                }
                            }
                        }
                        DefaultSpacer()
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = "Liczba miejsc",
                            onValueChange = {},
                            label = { Text("Event Name") },
                        )
                        DefaultSpacer()
                        Button(onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Add Event")
                        }
                        DefaultSpacer()
                        Button(onClick = {},
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun DefaultSpacer(){
    Spacer(Modifier.size(10.dp))
}