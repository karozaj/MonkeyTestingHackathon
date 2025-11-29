package com.example.monkeytestinghackathon.states

import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import java.time.LocalDateTime

data class EventListViewState  constructor(
    val isLoading: Boolean = false,
    val eventsList: List<CardGameEvent> = listOf(
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = LocalDateTime.now(),
            endTime =  LocalDateTime.now(),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  LocalDateTime.now(),
        ),
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = LocalDateTime.now(),
            endTime =  LocalDateTime.now(),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  LocalDateTime.now(),
        ),
        CardGameEvent(
            title = "Sample Event",
            description = "This is a sample event description.",
            category = EventType.CASUAL,
            location = Location.KATOWICE,
            gameType = CardGameTypes.FLESH_AND_BLOOD,
            startTime = LocalDateTime.now(),
            endTime =  LocalDateTime.now(),
            maxParticipants = 1,
            id = "",
            participantsCount = 0,
            createdAt =  LocalDateTime.now(),
        )
    ),

    )
