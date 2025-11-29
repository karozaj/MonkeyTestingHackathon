package com.example.monkeytestinghackathon.models

enum class EventType(
    val key: String,
    val value: String
) {
    CASUAL("casual", "Casual"),
    LEAGUE("league", "League"),
    TOURNAMENT("tournament", "Tournament");

    companion object {
        fun eventTypeFromKey(key: String): EventType? =
            EventType.entries.find { it.key == key }
    }
}