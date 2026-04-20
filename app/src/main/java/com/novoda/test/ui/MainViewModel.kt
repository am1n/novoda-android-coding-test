package com.novoda.test.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.novoda.test.model.User
import com.novoda.test.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainScreenUiState>(MainScreenUiState.Loading)
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    suspend fun getUsers() {
        _uiState.value = MainScreenUiState.Loading
        try {
            val users = repository.getUsers()
            _uiState.value = if (users.isEmpty())
                MainScreenUiState.Empty
            else
                MainScreenUiState.Success(users)
        } catch (e: Exception) {
            _uiState.value = MainScreenUiState.Error(e.message ?: "Unknown error in getting users")
        }
    }

}