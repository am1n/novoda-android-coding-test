package com.novoda.test.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.novoda.test.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    suspend fun getUsers() {
        _uiState.value = MainScreenUiState.Loading
        try {
            // TODO: get list of users from repository
            val users = listOf(
                User(
                    id = 1,
                    name = "Example User",
                    reputation = 5,
                    imageUrl = "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG",
                    followed = false
                )
            )
            _uiState.value = if (users.isEmpty())
                MainScreenUiState.Empty
            else
                MainScreenUiState.Success(users)
        } catch (e: Exception) {
            _uiState.value = MainScreenUiState.Error(e.message ?: "Unknown error in getting users")
        }
    }

}