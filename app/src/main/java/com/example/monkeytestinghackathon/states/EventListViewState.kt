package com.example.monkeytestinghackathon.states

import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import java.time.LocalDateTime
import java.util.Date

data class EventListViewState  constructor(
    val isLoading: Boolean = false,
    val eventsList: List<CardGameEvent> = listOf(
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = Date(0),
            endTime =  Date(0),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  Date(0),
        ),
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = Date(0),
            endTime =  Date(0),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  Date(0),
        ),
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = Date(0),
            endTime =  Date(0),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  Date(0),
        )
    ),

    )
