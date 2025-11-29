package com.example.monkeytestinghackathon.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames


@Serializable
data class CardGameEvent(
    val title: String,
    val description: String,
    val category: String,
    val location: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    @SerialName("max_participants")
    val maxParticipants: Long,
    val id: String,
    @SerialName("participants_count")
    val participantsCount: Long,
    @SerialName("created_at")
    val createdAt: String,
)
