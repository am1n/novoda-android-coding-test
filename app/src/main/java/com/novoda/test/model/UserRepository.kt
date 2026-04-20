package com.novoda.test.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val stackOverflowService: StackOverflowService,
    private val followedUsers: FollowedUsers
) {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val _followedIds: MutableStateFlow<Set<Int>> =
        MutableStateFlow(followedUsers.followedIds)

    val usersFlow: Flow<List<User>> = combine(_users, _followedIds) { users, followed ->
        users.map { user ->
            User(
                id = user.id,
                name = user.name,
                reputation = user.reputation,
                imageUrl = user.imageUrl,
                followed = user.id in followed
            )
        }
    }

    suspend fun getUsers() {
        val userResultsJson = stackOverflowService.getUsers()
        val users = userResultsJson.items.map {
            val isFollowed = _followedIds.value.contains(it.userId)
            User(
                id = it.userId,
                name = it.displayName,
                reputation = it.reputation,
                imageUrl = it.profileImage,
                followed = isFollowed
            )
        }
        _users.value = users
    }

    fun setUserFollowed(userId: Int, followed: Boolean) {
        followedUsers.setUserFollowed(userId, followed)
        val updated = _followedIds.value.toMutableSet().apply {
            if (followed) {
                add(userId)
            } else {
                remove(userId)
            }
        }
        _followedIds.value = updated
    }
}