package com.example.monkeytestinghackathon.models

data class EventsList(
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    val game_type: String,
    val start_time: String,
    val end_time: String? = null,
    val max_participants: Int? = null,
    val id: String,
    val participants_count: Int,
    val created_at: String,
    val semantic_score: Double,
    val recency_score: Double,
    val popularity_score: Double,
    val final_score: Double,
    val llm_reason: String
)