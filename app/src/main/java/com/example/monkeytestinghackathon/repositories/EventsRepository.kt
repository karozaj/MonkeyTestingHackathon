package com.example.monkeytestinghackathon.repositories

import android.util.Log
import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.models.CreateUserRequest
import com.example.monkeytestinghackathon.models.EventsResponse
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.models.JoinLeaveEvent
import com.example.monkeytestinghackathon.models.OneEvent
import com.example.monkeytestinghackathon.models.toCardGameEvent
import com.example.monkeytestinghackathon.network.RetrofitInstance
import com.example.monkeytestinghackathon.networking.EventsClient
import retrofit2.Response

class EventsRepository {

    suspend fun getFeed(userId: String): Response<FeedResponse> {
        return RetrofitInstance.api.getFeed(userId)
    }

    suspend fun getEventsByLocalization(localization: String): Response<EventsResponse> {
        return RetrofitInstance.api.getEventsByLocalization(localization)
    }

    suspend fun getEventsByCategory(category: String): Response<FeedResponse> {
        return RetrofitInstance.api.getEventsByCategory(category)
    }

    suspend fun getEventsByLocalizationAndCategory(localization: String, category: String): Response<FeedResponse> {
        return RetrofitInstance.api.getEventsByLocalizationAndCategory(localization, category)
    }

    suspend fun getEvent(eventId: String): Response<OneEvent> {
        return RetrofitInstance.api.getEvent(eventId)
    }

    suspend fun joinEvent(eventId: String, userId: String): Response<JoinLeaveEvent> {
        return RetrofitInstance.api.joinEvent(eventId, userId)
    }

    suspend fun leaveEvent(eventId: String, userId: String): Response<JoinLeaveEvent> {
        return RetrofitInstance.api.leaveEvent(eventId, userId)
    }

}