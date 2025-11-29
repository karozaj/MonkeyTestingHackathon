package com.example.monkeytestinghackathon.models

enum class EventType(
    val key: String,
    val value: String
) {
    CASUAL_PLAY("casual", "Casual Play"),
    LEAGUE("league", "League"),
    EVENT("event", "Event"),
    TOURNAMENT("tournament", "Tournament");

    companion object {
        fun eventTypeFromKey(key: String): EventType? =
            EventType.entries.find { it.key == key }
    }
}