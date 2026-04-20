package com.novoda.test.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.novoda.test.R
import com.novoda.test.model.User
import com.novoda.test.ui.theme.NovodaAndroidCodingTestTheme
import java.text.DecimalFormat

@Preview(showBackground = true)
@Composable
fun MainScreenEmptyPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            uiState = MainScreenUiState.Empty
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenLoadingPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            uiState = MainScreenUiState.Loading
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenErrorPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            uiState = MainScreenUiState.Error("404 - not found")
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenSuccessPreview() {
    NovodaAndroidCodingTestTheme {
        MainScreen(
            uiState = MainScreenUiState.Success(
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
        uiState = uiState.value
    ) { user ->
        viewModel.triggerUserFollowed(user)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    uiState: MainScreenUiState,
    onFollowClick: ((User) -> Unit)? = null
) {
    when (uiState) {
        MainScreenUiState.Empty -> {
            Text(
                modifier = modifier,
                text = stringResource(R.string.state_empty_title)
            )
        }

        is MainScreenUiState.Error -> {
            Column(modifier = modifier) {
                Text(text = stringResource(R.string.state_error_title))
                Text(text = uiState.message)
            }
        }

        MainScreenUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is MainScreenUiState.Success -> {
            LazyColumn(
                modifier = modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.users) { user ->
                    UserItem(
                        modifier = Modifier.fillMaxWidth(),
                        user = user,
                        onFollowClick = onFollowClick
                    )
                }
            }
        }
    }
}

@Composable
private fun UserItem(
    modifier: Modifier = Modifier,
    user: User,
    onFollowClick: ((User) -> Unit)? = null
) {
    Row(
        modifier = modifier
            .background(if (user.followed) Color.DarkGray else Color.Transparent)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .size(80.dp)
                .background(Color.Gray),
            model = user.imageUrl,
            contentDescription = null,
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(user.name, fontWeight = FontWeight.Bold)
            Text(stringResource(R.string.reputation_format, user.reputation.formattedReputation()))
        }
        Button(onClick = { onFollowClick?.invoke(user) }) {
            val label = if (user.followed) R.string.button_unfollow else R.string.button_follow
            Text(text = stringResource(label))
        }
    }
}

private val reputationNumberFormat = DecimalFormat("#,###")
private fun Int.formattedReputation(): String {
    return reputationNumberFormat.format(this)
}
