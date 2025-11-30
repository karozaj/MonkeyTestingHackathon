package com.example.monkeytestinghackathon.models

data class OneEvent(
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val location: String = "",
    val game_type: String = "",
    val start_time: String = "",
    val end_time: String? = null,
    val max_participants: Int? = null,
    val id: String = "",
    val participants_count: Int = 0,
    val created_at: String = ""
)
