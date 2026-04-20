package com.novoda.test.model

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserRepositoryTest {

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
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
        repository = UserRepository(service)
    }

    @Test
    fun getUsers() = runTest {
        val users = repository.getUsers()
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