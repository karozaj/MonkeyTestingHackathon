package com.example.monkeytestinghackathon.repositories

import com.example.monkeytestinghackathon.models.CreateUserRequest
import com.example.monkeytestinghackathon.network.RetrofitInstance
import retrofit2.Response

class RegisterRepository {

    suspend fun createUser(request: CreateUserRequest): Response<Unit> {
        return RetrofitInstance.api.createUser(request)
    }
}