package com.novoda.test.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {

    private lateinit var context: Context
    private lateinit var followedUsers: FollowedUsers
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        followedUsers = FollowedUsers(context)
        val service = object : StackOverflowService {
            override suspend fun getUsers(): UserResultsJson = UserResultsJson(
                items = listOf(
                    UserJson(
                        userId = 1,
                        displayName = "Example 1",
                        reputation = 1000,
                        profileImage = "https://image1.png"
                    ),
                    UserJson(
                        userId = 2,
                        displayName = "Example 2",
                        reputation = 2000,
                        profileImage = "https://image2.png"
                    )
                )
            )
        }
        repository = UserRepository(
            stackOverflowService = service,
            followedUsers = followedUsers
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getUsers() = runTest {
        repository.usersFlow.test {
            val first = awaitItem()
            assertThat(first).isEmpty()

            repository.getUsers()
            val users = awaitItem()
            assertThat(users.size).isEqualTo(2)

            assertThat(users[0]).isEqualTo(
                User(
                    id = 1,
                    name = "Example 1",
                    reputation = 1000,
                    imageUrl = "https://image1.png",
                    followed = false
                )
            )

            assertThat(users[1]).isEqualTo(
                User(
                    id = 2,
                    name = "Example 2",
                    reputation = 2000,
                    imageUrl = "https://image2.png",
                    followed = false
                )
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun setUserFollowed() = runTest {
        // follow user 2
        repository.setUserFollowed(2, true)

        repository.usersFlow.test {
            val first = awaitItem()
            assertThat(first).isEmpty()

            repository.getUsers()
            var users = awaitItem()
            assertThat(users.size).isEqualTo(2)

            assertThat(users[0]).isEqualTo(
                User(
                    id = 1,
                    name = "Example 1",
                    reputation = 1000,
                    imageUrl = "https://image1.png",
                    followed = false
                )
            )

            assertThat(users[1]).isEqualTo(
                User(
                    id = 2,
                    name = "Example 2",
                    reputation = 2000,
                    imageUrl = "https://image2.png",
                    followed = true
                )
            )

            // unfollow user 2
            repository.setUserFollowed(2, false)

            users = awaitItem()
            assertThat(users.size).isEqualTo(2)

            assertThat(users[0]).isEqualTo(
                User(
                    id = 1,
                    name = "Example 1",
                    reputation = 1000,
                    imageUrl = "https://image1.png",
                    followed = false
                )
            )

            assertThat(users[1]).isEqualTo(
                User(
                    id = 2,
                    name = "Example 2",
                    reputation = 2000,
                    imageUrl = "https://image2.png",
                    followed = false
                )
            )
        }

    }
}