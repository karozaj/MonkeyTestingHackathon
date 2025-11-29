package com.example.monkeytestinghackathon.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monkeytestinghackathon.models.CardGameTypes
import com.example.monkeytestinghackathon.models.EventType
import com.example.monkeytestinghackathon.models.Location
import com.example.monkeytestinghackathon.repositories.EventsRepository
import com.example.monkeytestinghackathon.states.AddEventViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddEventViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddEventViewState())
    val state = _state.asStateFlow()

    val testReposit = EventsRepository()

    fun test(){
        viewModelScope.launch {
           val result = testReposit.getEventById("0678aa4b-5ca2-4085-ba51-d1b7c8152a4e")
            if(result==null) {
                Log.i("EventsRepository", "Failed to convert dto")
            }
            else{
                Log.i("EventsRepository", "Converted to successffully")
            }
        }
    }

    fun canAddEvent(): Boolean {
        Log.i("TEST","canAddEvent called")
        test()
        val currentState = _state.value
        return currentState.title.isNotBlank() &&
                currentState.maxParticipants > 0 && currentState.participantsString.toIntOrNull()!=null
                && currentState.maxParticipants<=50
    }

    fun updateTitle(title: String) {
        val maxCharacters = 32
        _state.update { it.copy(title = title.take(maxCharacters)) }
    }

    fun updateDescription(description: String) {
        val maxCharacters = 256
        _state.update { it.copy(description = description.take(maxCharacters)) }
    }

    fun updateCategory(category: EventType) {
        _state.update { it.copy(category = category) }
    }

    fun updateLocation(location: Location) {
        _state.update { it.copy(location = location) }
    }

    fun updateGameType(gameType: CardGameTypes) {
        _state.update { it.copy(gameType = gameType) }
    }


    fun updateStartTime(startTime: Long?) {
        if(startTime!=null) {
            _state.update { it.copy(startTime = Date(startTime)) }
        }
    }


    fun updateStartTime(startTime: Date) {
        _state.update { it.copy(startTime = startTime) }
    }

    fun updateEndTime(endTime: Date) {
        _state.update { it.copy(endTime = endTime) }
    }

    fun updateMaxParticipants(maxParticipants: Int) {
        val maxNumber = 50
        _state.update { it.copy(maxParticipants = minOf(maxParticipants,maxNumber)) }
    }

    fun updateId(id: String) {
        _state.update { it.copy(id = id) }
    }

    fun updateParticipantsCount(participantsCount: Int) {
        val maxNumber = 50
        _state.update { it.copy(participantsCount = minOf(participantsCount,maxNumber)) }
    }

    fun updateCreatedAt(createdAt: Date) {
        _state.update { it.copy(createdAt = createdAt) }
    }

    fun updateParticipantsString(participantsString: String) {
        _state.update { it.copy(participantsString = participantsString) }
        if(participantsString.toIntOrNull()!=null) {
            updateParticipantsCount(participantsString.toInt())
        }
    }
}