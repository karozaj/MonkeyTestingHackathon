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

    private val _feedresponse = MutableStateFlow<List<FeedResponse>>(emptyList())
    private val _events = MutableStateFlow<List<Events>>(emptyList())

    val events: StateFlow<List<Events>> = _events




    suspend fun getEvents(userId: String) {
            try {
                val response = repo.getFeed(userId)
                if (response.isSuccessful) {
                    val body = response.body()
                    _feedresponse.value = if (body != null) listOf(body) else emptyList()
                    for (item in _feedresponse.value) {
                        _events.value = item.events
                    }
                } else {
                    _feedresponse.value = emptyList()
                }
            } catch (e: Exception) {
                _feedresponse.value = emptyList()
            }
    }
}