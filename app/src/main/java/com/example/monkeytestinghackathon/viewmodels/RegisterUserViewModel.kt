package com.example.monkeytestinghackathon.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monkeytestinghackathon.models.CreateUserRequest
import com.example.monkeytestinghackathon.repositories.RegisterRepository
import kotlinx.coroutines.launch

class RegisterUserViewModel(): ViewModel() {

    val repo: RegisterRepository = RegisterRepository()

    fun createUserAccount(
        user_id: String,
        username: String,
        location: String,
        description: String,
        preferred_categories: List<String>,
        preferred_game_types: List<String>,
        onComplete: (Boolean) -> Unit)
    {
        val body = CreateUserRequest(
            user_id = user_id,
            username = username,
            location = location,
            description = description,
            preferred_categories = preferred_categories,
            preferred_game_types = preferred_game_types
        )

        viewModelScope.launch {
            runCatching { repo.createUser(body) }
                .onSuccess { onComplete(it.isSuccessful) }
                .onFailure { onComplete(false) }
        }

    }
}