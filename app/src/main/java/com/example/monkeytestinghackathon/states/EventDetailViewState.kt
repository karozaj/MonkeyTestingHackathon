package com.example.monkeytestinghackathon.states

import com.example.monkeytestinghackathon.models.OneEvent

data class EventDetailViewState(
    val event: OneEvent = OneEvent(
        title = "",
        description = "",
        category = "",
        location = "",
        game_type = "",
        start_time = "",
        end_time = null,
        max_participants = null,
        id = "",
        participants_count = 0,
        created_at = ""
    )
)
