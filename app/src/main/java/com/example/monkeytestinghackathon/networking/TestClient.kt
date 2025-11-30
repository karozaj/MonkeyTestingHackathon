package com.example.monkeytestinghackathon.networking

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class TestClient {
    val client = HttpClient()
    val path = "http://192.168.3.113:8000/health"
    suspend fun test(){
        val response = client.get(path) {}
        Log.i("TestClient", "Response: $response")
    }
}