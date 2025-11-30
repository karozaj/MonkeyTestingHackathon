package com.example.monkeytestinghackathon.networking

import io.ktor.client.HttpClient

fun getDefaultHttpClient(): HttpClient{
    return HttpClient()
}