package com.example.monkeytestinghackathon.models

data class CreateUserRequest(
    val user_id: String,
    val username: String,
    val location: String,
    val description: String,
    val preferred_categories: List<String>,
    val preferred_game_types: List<String>
)
