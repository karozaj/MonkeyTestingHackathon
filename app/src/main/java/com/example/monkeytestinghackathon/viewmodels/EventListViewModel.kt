package com.example.monkeytestinghackathon.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkeytestinghackathon.models.Events
import com.example.monkeytestinghackathon.models.FeedResponse
import com.example.monkeytestinghackathon.repositories.EventsRepository
import com.example.monkeytestinghackathon.states.EventListViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventListViewModel(): ViewModel() {

    val repo: EventsRepository = EventsRepository()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _feedresponse = MutableStateFlow<List<FeedResponse>>(emptyList())
    private val _events = MutableStateFlow<List<Events>>(emptyList())

    val events: StateFlow<List<Events>> = _events




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