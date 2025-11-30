package com.example.monkeytestinghackathon.models

data class FeedResponse(
    val events: List<Events>,
    val total: Int,
    val has_more: Boolean
)
