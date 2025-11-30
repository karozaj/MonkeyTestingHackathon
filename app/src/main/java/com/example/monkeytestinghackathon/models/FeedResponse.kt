package com.example.monkeytestinghackathon.models

import kotlinx.serialization.SerialName

data class FeedResponse(
    val events: List<Events>,
    val total: Int,
     val has_more: Boolean
)
