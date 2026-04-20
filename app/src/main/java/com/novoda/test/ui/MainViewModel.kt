package com.novoda.test.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.novoda.test.model.User
import com.novoda.test.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.usersFlow.collect { users ->
                _uiState.value = if (users.isEmpty()) {
                    MainScreenUiState.Empty
                } else {
                    MainScreenUiState.Success(users)
                }
            }
        }
    }

    suspend fun getUsers() {
        _uiState.value = MainScreenUiState.Loading
        try {
            repository.getUsers()
        } catch (e: Exception) {
            _uiState.value = MainScreenUiState.Error(e.message ?: "Unknown error in getting users")
        }
    }

    fun triggerUserFollowed(user: User) {
        val followed = !user.followed
        repository.setUserFollowed(user.id, followed)
    }

}