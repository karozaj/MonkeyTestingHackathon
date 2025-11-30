package com.example.monkeytestinghackathon.viewmodels

import androidx.lifecycle.ViewModel
import com.example.monkeytestinghackathon.models.EventsList
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.repositories.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventListViewModel(): ViewModel() {

    val repo: EventsRepository = EventsRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _feedresponse = MutableStateFlow<List<FeedResponse>>(emptyList())
    private val _events = MutableStateFlow<List<EventsList>>(emptyList())

    val events: StateFlow<List<EventsList>> = _events




    suspend fun getEvents(userId: String) {
        _isLoading.value = true
        try {
            val response = repo.getFeed(userId)
            if (response.isSuccessful) {
                val body = response.body()
                _feedresponse.value = body?.let { listOf(it) } ?: emptyList()
                _events.value = _feedresponse.value.firstOrNull()?.events ?: emptyList()
            } else {
                _events.value = emptyList()
            }
        } catch (e: Exception) {
            _events.value = emptyList()
        }
        _isLoading.value = false
    }

}