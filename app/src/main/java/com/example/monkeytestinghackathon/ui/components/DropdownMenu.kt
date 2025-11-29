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
//        expanded = expanded,
//        onExpandedChange = { gameDropdownExpanded = !gameDropdownExpanded }
//    ) {
//        TextField(
//            value = "test",
//            onValueChange = {},
//            readOnly = true,
//            label = { Text("Select your nearest city") },
//            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = gameDropdownExpanded) },
//            modifier = Modifier.menuAnchor()
//        )
//        ExposedDropdownMenu(
//            expanded = gameDropdownExpanded,
//            onDismissRequest = { gameDropdownExpanded = false }
//        ) {
//            CardGameTypes.entries.forEach { game ->
//                DropdownMenuItem(
//                    text = { Text(game.name) },
//                    onClick = {
//
//                    }
//                )
//            }
//        }
//    }
//}