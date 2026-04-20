package com.novoda.test.model

import javax.inject.Inject

class UserRepository @Inject constructor(
    private val stackOverflowService: StackOverflowService
) {

    suspend fun getUsers(): List<User> {
        val userResultsJson = stackOverflowService.getUsers()
        val users = userResultsJson.items.map {
            User(
                id = it.userId,
                name = it.displayName,
                reputation = it.reputation,
                imageUrl = it.profileImage,
                followed = false // TODO: get followed status correctly
            )
        }
        return users
    }
}