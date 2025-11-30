package com.example.monkeytestinghackathon.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkeytestinghackathon.models.Events
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.repositories.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventListViewModel(): ViewModel() {

    val repo: EventsRepository = EventsRepository()

    private val _feedresponse = MutableStateFlow<List<FeedResponse>>(emptyList())
    private val _eventsResponse = MutableStateFlow<List<FeedResponse>>(emptyList())
    private val _events = MutableStateFlow<List<Events>>(emptyList())

    val events: StateFlow<List<Events>> = _events

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading







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
                Log.e("EventListViewModel", "Error fetching events: ${response.code()}")
            }
        } catch (e: Exception) {
            _events.value = emptyList()
            Log.e("EventListViewModel", "Error fetching events", e)
        }
        if (_events.value.isEmpty()) {
            Log.d("EventListViewModel", "No events found")
        }
        _isLoading.value = false
    }

    fun getEventsByLocalization(localization: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repo.getEventsByLocalization(localization)
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventsResponse.value = body?.let { listOf(it) } ?: emptyList()
                    _events.value = (_eventsResponse.value.firstOrNull()?.events ?: emptyList()) as List<Events>
                } else {
                    _events.value = emptyList()
                    Log.e("EventListViewModel", "Error fetching events: ${response.code()}")
                }
            } catch (e: Exception) {
                _events.value = emptyList()
                Log.e("EventListViewModel", "Error fetching events", e)
            }
            if (_events.value.isEmpty()) {
                Log.d("EventListViewModel", "No events found")
            }
            _isLoading.value = false
        }
    }



}