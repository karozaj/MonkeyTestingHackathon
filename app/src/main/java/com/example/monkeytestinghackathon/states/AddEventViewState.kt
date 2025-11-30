package com.example.monkeytestinghackathon.states

import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

data class AddEventViewState(
    val title: String="",
    val description: String="",
    val category: EventType= EventType.TOURNAMENT,
    val location: Location = Location.KATOWICE,
    val gameType: CardGameTypes = CardGameTypes.FLESH_AND_BLOOD,
    val startTime: Date = Calendar.getInstance().time,
    val endTime: Date = Calendar.getInstance().time,
    val maxParticipants: Int = 5,
    val id: String = "",
    val participantsCount: Int = 0,
    val createdAt: Date = Calendar.getInstance().time,

    val participantsString: String = "5",
)
