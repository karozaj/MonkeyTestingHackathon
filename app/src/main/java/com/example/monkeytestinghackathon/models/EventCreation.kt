package com.example.monkeytestinghackathon.models

import java.util.Date

data class EventCreation(
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    val game_type: String,
    val start_time: String,
    val end_time: String,
    val max_participants: Int
)
