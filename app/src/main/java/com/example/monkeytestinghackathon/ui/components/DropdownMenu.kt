//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.ui.Modifier
//import com.example.monkeytestinghackathon.models.Location
//
//package com.example.monkeytestinghackathon.ui.components
//
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExposedDropdownMenuBox
//import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import com.example.monkeytestinghackathon.models.CardGameTypes
//
//@Composable
//fun DropdownMenu(
//    expanded: Boolean,
//) {
//    ExposedDropdownMenuBox(
//    modifier = Modifier.fillMaxWidth(),
//    expanded = expanded,
//    onExpandedChange = { expandDropdown(locationDropdown = !locationDropdownExpanded) }
//    ) {
//        TextField(
//
//            value = "test",
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Select your nearest city") },
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = locationDropdownExpanded) },
//            modifier = Modifier.menuAnchor().fillMaxWidth()
//        )
//        ExposedDropdownMenu(
//            expanded = locationDropdownExpanded,
//            onDismissRequest = { locationDropdownExpanded = false }
//        ) {
//            Location.entries.forEach { location ->
//                DropdownMenuItem(
//                    text = { Text(location.name) },
//                    onClick = {
//
//                    }
//                )
//            }
//        }
//    }
//}