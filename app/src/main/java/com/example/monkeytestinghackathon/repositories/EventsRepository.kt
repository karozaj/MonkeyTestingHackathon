package com.example.monkeytestinghackathon.repositories

import android.util.Log
import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.models.CreateUserRequest
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.models.toCardGameEvent
import com.example.monkeytestinghackathon.network.RetrofitInstance
import com.example.monkeytestinghackathon.networking.EventsClient
import retrofit2.Response

class EventsRepository {

    suspend fun getFeed(userId: String): Response<FeedResponse> {
        return RetrofitInstance.api.getFeed(userId)
    }
}