package com.example.monkeytestinghackathon.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.monkeytestinghackathon.models.JoinLeaveEvent
import com.example.monkeytestinghackathon.models.OneEvent
import com.example.monkeytestinghackathon.repositories.EventsRepository
import com.example.monkeytestinghackathon.states.EventDetailViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventDetailViewModel: ViewModel() {
    val repo: EventsRepository = EventsRepository()

    private val _state = MutableStateFlow(EventDetailViewState())
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow<OneEvent>(OneEvent())
    val event: StateFlow<OneEvent> = _event

    private val _joinEvent = MutableStateFlow<JoinLeaveEvent>(JoinLeaveEvent())
    val joinEvent: StateFlow<JoinLeaveEvent> = _joinEvent

    private val _leaveEvent = MutableStateFlow<JoinLeaveEvent>(JoinLeaveEvent())
    val leaveEvent: StateFlow<JoinLeaveEvent> = _leaveEvent


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun updateEvent(newevent: OneEvent) {
        _state.update {
            it.copy(
                event = newevent
            )
        }
    }

    suspend fun getEvent(eventId: String): OneEvent? {
        _isLoading.value = true
        try {
            val response = repo.getEvent(eventId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return body
                    //_event.value = body
                } else {
                    Log.e("EventDetailViewModel", "Error fetching event: response body is null")
                }
            } else {
                Log.e("EventDetailViewModel", "Error fetching event: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error fetching event", e)

        }
        return null
        _isLoading.value = false
    }

    fun joinEventAsync(eventId: String, userId: String){
        viewModelScope.launch {
            joinEvent(eventId,userId)
        }
    }


    fun leaveEventAsync(eventId: String, userId: String){
        viewModelScope.launch {
            leaveEvent(eventId,userId)
        }
    }

    suspend fun joinEvent(eventId: String, userId: String) {
        _isLoading.value = true
        try {
            val response = repo.joinEvent(eventId, userId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _joinEvent.value = body
                } else {
                    Log.e("EventDetailViewModel", "Error joining event: response body is null")
                }
            } else {
                Log.e("EventDetailViewModel", "Error joining event: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error joining event", e)
        }
        _isLoading.value = false
    }

    suspend fun leaveEvent(eventId: String, userId: String) {
        _isLoading.value = true
        try {
            val response = repo.leaveEvent(eventId, userId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _leaveEvent.value = body
                } else {
                    Log.e("EventDetailViewModel", "Error leaving event: response body is null")
                }
            } else {
                Log.e("EventDetailViewModel", "Error leaving event: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error leaving event", e)
        }
        _isLoading.value = false
    }
}



