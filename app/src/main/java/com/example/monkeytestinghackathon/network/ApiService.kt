package com.example.monkeytestinghackathon.network

import com.example.monkeytestinghackathon.models.CreateUserRequest
import com.example.monkeytestinghackathon.models.Events
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.models.JoinLeaveEvent
import com.example.monkeytestinghackathon.models.OneEvent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    //FEED
    @GET("feed/{user_id}?limit=20&offset=0")
    suspend fun getFeed(@Path("user_id") userId: String): Response<FeedResponse>

    //EVENTS
    @GET("events/{event_id}")
    suspend fun getEvent(@Path("event_id") eventId: String): Response<OneEvent>

    @POST("events/{event_id}/join?user_id={user_id}")
    suspend fun joinEvent(@Path("event_id") eventId: String, @Path("user_id") userId: String): Response<JoinLeaveEvent>

    @POST("events/{event_id}/leave?user_id={user_id}")
    suspend fun leaveEvent(@Path("event_id") eventId: String, @Path("user_id") userId: String): Response<JoinLeaveEvent>
    //USER
    @POST("users/")
    suspend fun createUser(@Body body: CreateUserRequest): Response<Unit>

    @GET("users/{user_id}")
    suspend fun getUser(@Path("user_id") user_id: String): Response<CreateUserRequest>
}