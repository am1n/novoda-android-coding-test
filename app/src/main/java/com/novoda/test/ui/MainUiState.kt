package com.novoda.test.ui

import com.novoda.test.model.User

sealed class MainScreenUiState {

    object Loading : MainScreenUiState()

    object Empty : MainScreenUiState()

    data class Success(val users: List<User>) : MainScreenUiState()

    data class Error(val message: String) : MainScreenUiState()
}
