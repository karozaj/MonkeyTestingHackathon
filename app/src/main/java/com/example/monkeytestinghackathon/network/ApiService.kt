package com.example.monkeytestinghackathon.network

import com.example.monkeytestinghackathon.models.CreateUserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("users/")
    suspend fun createUser(@Body body: CreateUserRequest): Response<Unit>
}