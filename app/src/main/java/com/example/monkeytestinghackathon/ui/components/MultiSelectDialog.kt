// Kotlin
package com.example.monkeytestinghackathon.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> MultiSelectDialog(
    title: String,
    allItems: List<T>,
    initialSelection: Set<T>,
    label: (T) -> String,
    onConfirm: (Set<T>) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelection by remember { mutableStateOf(initialSelection) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
            ) {
                items(allItems) { item ->
                    val checked = item in tempSelection
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                tempSelection = if (checked) tempSelection - item else tempSelection + item
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(label(item))
                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                tempSelection = if (it) tempSelection + item else tempSelection - item
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempSelection) }) {
                Text("Zatwierdź")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Anuluj")
            }
        }
    )
}
