package com.example.monkeytestinghackathon.viewmodels

import androidx.lifecycle.ViewModel
import com.example.monkeytestinghackathon.models.CardGameEvent
import com.example.monkeytestinghackathon.states.EventListViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EventListViewModel(): ViewModel() {
    private val _state = MutableStateFlow(EventListViewState())
    val state = _state.asStateFlow()



    fun updateEventList(newList: List<CardGameEvent>) {
        _state.update {
            it.copy(
                eventsList = newList
            )
        }
    }

    fun updateIsLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                isLoading = isLoading
            )
        }
    }
}