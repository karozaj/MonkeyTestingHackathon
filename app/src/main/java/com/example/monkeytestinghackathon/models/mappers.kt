package com.example.monkeytestinghackathon.models

import com.example.monkeytestinghackathon.networking.dto.CardGameEventDTO
import com.example.monkeytestinghackathon.utils.stringToDate
import java.util.Date

fun CardGameEventDTO.toCardGameEvent(): CardGameEvent {
    return CardGameEvent(
        id = this.id,
        title = this.title,
        description = this.description,
        category = EventType.eventTypeFromKey(this.category)?: EventType.CASUAL,
        location = Location.locationFromKey(this.location)?: Location.KATOWICE,
        gameType = CardGameTypes.gameTypesFromKey(this.gameType)?: CardGameTypes.FLESH_AND_BLOOD,
        startTime = stringToDate(this.startTime),
        endTime = stringToDate(this.endTime),
        maxParticipants = this.maxParticipants.toInt(),
        participantsCount = this.participantsCount.toInt(),
        createdAt = stringToDate(this.createdAt)
    )
}