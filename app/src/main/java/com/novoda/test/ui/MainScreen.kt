package com.novoda.test.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.novoda.test.model.User
import com.novoda.test.ui.theme.NovodaAndroidCodingTestTheme

@Preview(showBackground = true)
@Composable
fun MainScreenEmptyPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            state = MainScreenUiState.Empty
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            state = MainScreenUiState.Loading
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenErrorPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            state = MainScreenUiState.Error("An error occurred")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenSuccessPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            state = MainScreenUiState.Success(
                listOf(
                    User(
                        id = 1,
                        name = "Example User",
                        reputation = 5,
                        imageUrl = "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG",
                        followed = false
                    )
                )
            )
        )
    }
}

@Composable
fun MainScreenRoute(
    modifier: Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }
    MainScreen(
        modifier = modifier,
        state = uiState.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainScreenUiState
) {
    
}
