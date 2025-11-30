package com.example.monkeytestinghackathon.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.monkeytestinghackathon.models.JoinLeaveEvent
import com.example.monkeytestinghackathon.models.OneEvent
import com.example.monkeytestinghackathon.repositories.EventsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventDetailViewModel: ViewModel() {
    val repo: EventsRepository = EventsRepository()

    private val _event = MutableStateFlow<OneEvent>(OneEvent())
    val event: StateFlow<OneEvent> = _event

    private val _joinEvent = MutableStateFlow<JoinLeaveEvent>(JoinLeaveEvent())
    val joinEvent: StateFlow<JoinLeaveEvent> = _joinEvent

    private val _leaveEvent = MutableStateFlow<JoinLeaveEvent>(JoinLeaveEvent())
    val leaveEvent: StateFlow<JoinLeaveEvent> = _leaveEvent


    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading


    suspend fun getEvent(eventId: String) {
        _isLoading.value = true
        try {
            val response = repo.getEvent(eventId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    _event.value = body
                } else {
                    Log.e("EventDetailViewModel", "Error fetching event: response body is null")
                }
            } else {
                Log.e("EventDetailViewModel", "Error fetching event: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("EventDetailViewModel", "Error fetching event", e)

        }
        _isLoading.value = false
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



