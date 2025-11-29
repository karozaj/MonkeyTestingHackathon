package com.example.monkeytestinghackathon.models

import java.time.LocalDateTime

data class CardGameEvent(
    val title: String,
    val description: String,
    val category: EventType,
    val location: Location,
    val gameType: CardGameTypes,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val maxParticipants: Int,
    val id: String,
    val participantsCount: Int,
    val createdAt: LocalDateTime,
)
//@Serializable
//data class CardGameEvent(
//    val title: String,
//    val description: String,
//    val category: String,
//    val location: String,
//    @SerialName("game_type")
//    val gameType:String,
//    @SerialName("start_time")
//    val startTime: String,
//    @SerialName("end_time")
//    val endTime: String,
//    @SerialName("max_participants")
//    val maxParticipants: Long,
//    val id: String,
//    @SerialName("participants_count")
//    val participantsCount: Long,
//    @SerialName("created_at")
//    val createdAt: String,
//)
