package com.example.monkeytestinghackathon.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.monkeytestinghackathon.ui.theme.Typography


@Composable
/** Horizontal row representing a single event
 * @param [index] Index of the event in the list
 * @param [title] Title of the event
 * @param [gameName] Name of the game associated with the event
 * @param [location] Location of the event (optional)
 * @param [date] Date of the event (optional)
 * @param [currentParticipants] Current number of participants in the event
 * @param [maxParticipants] Maximum number of participants allowed in the event
 * @param [onClick] Lambda function to be invoked when the row is clicked, passing the event index
 * */
fun EventRow(
    index: Int,
    title:String,
    gameName:String,
    location:String?,
    date:String?,
    currentParticipants:Int,
    maxParticipants:Int,
    onClick: ((Int) -> Unit),
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(enabled = index > 0) { onClick(index) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = Typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = gameName,
                    style = Typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val info = listOfNotNull(location, date).joinToString(", ")

                if (info.isNotEmpty()) {
                    Text(
                        text = info,
                        style = Typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Prawa strona – licznik uczestników
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "$currentParticipants / $maxParticipants",
                    style = Typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
