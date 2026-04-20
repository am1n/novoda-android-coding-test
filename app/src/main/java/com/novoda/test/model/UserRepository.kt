package com.novoda.test.model

import javax.inject.Inject

class UserRepository @Inject constructor() {

    suspend fun getUsers(): List<User> {
        val users = listOf(
            User(
                id = 1,
                name = "Example User",
                reputation = 5,
                imageUrl = "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG",
                followed = false
            ),
            User(
                id = 2,
                name = "Example User 2",
                reputation = 7,
                imageUrl = "https://www.gravatar.com/avatar/a007be5a61f6aa8f3e85ae2fc18dd66e?d=identicon&r=PG",
                followed = true
            )
        )
        return users
    }
}