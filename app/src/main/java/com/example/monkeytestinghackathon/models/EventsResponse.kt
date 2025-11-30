package com.example.monkeytestinghackathon.models

data class EventsResponse(
    val events: List<OneEvent>,
    val total: Int,
    val has_more: Boolean
)
