package com.example.monkeytestinghackathon.models

data class GetUser(
    val user_id: String,
    val username: String,
    val location: String,
    val description: String,
    val preferred_categories: List<String>,
    val preferred_game_types: List<String>,
    val id: String,
    val created_at: String
)
