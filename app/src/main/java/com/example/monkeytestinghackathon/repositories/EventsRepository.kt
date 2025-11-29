package com.example.monkeytestinghackathon.repositories

import android.util.Log
import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.models.toCardGameEvent
import com.example.monkeytestinghackathon.networking.EventsClient

class EventsRepository {
    val eventsClient = EventsClient()

    suspend fun getEventById(eventId: String): CardGameEvent? {
        val result = eventsClient.getEventById(eventId)
        Log.i("EventsRepository", "result: $result")
        if (result.isSuccess) {
            val dto = result.getOrNull()
            if (dto != null) {
                Log.i("EventsRepository", "Fetched event: $dto")
                return dto.toCardGameEvent()
            } else { Log.i("EventsRepository", "DTO is null")}
        }
        return null
    }
}