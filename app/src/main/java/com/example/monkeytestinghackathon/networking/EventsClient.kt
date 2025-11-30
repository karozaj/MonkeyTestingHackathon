package com.example.monkeytestinghackathon.networking

import android.util.Log
import com.example.monkeytestinghackathon.networking.dto.CardGameEventDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class EventsClient {
    val client = HttpClient(){
        install(ContentNegotiation){
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        encodeDefaults = true
                    }
                )
            }
        }

    val path = "http://10.0.2.2:8000/"

    suspend fun getEventById(id: String): Result<CardGameEventDTO> = runCatching {
        val response = client.get(path+"api/v1/events/"+id)
        if(response.status.isSuccess()){
            Log.i("EventsClient", "Successfully fetched event with id $id")
            response.body<CardGameEventDTO>()
        } else {
            Log.i("EventsClient", "Failed to fetch event with id $id")
            throw Exception("Failed to fetch event with id $id: ${response.status}")
        }
    }
}