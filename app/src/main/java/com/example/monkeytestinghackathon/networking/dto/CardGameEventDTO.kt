package com.example.monkeytestinghackathon.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardGameEventDTO(
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    @SerialName("game_type")
    val gameType: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("max_participants")
    val maxParticipants: Int,
    val id: String,
    @SerialName("participants_count")
    val participantsCount: Int,
    @SerialName("created_at")
    val createdAt: String,
)
