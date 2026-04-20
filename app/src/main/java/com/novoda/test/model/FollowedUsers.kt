package com.novoda.test.model

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class FollowedUsers @Inject constructor(
    private val context: Context
) {

    private val preferences by lazy {
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    val followedIds: Set<Int>
        get() = preferences.all.filter { it.value == true }.map { it.key.toInt() }.toSet()

    fun isUserFollowed(userId: Int): Boolean {
        val key = userId.toString()
        return preferences.getBoolean(key, false)
    }

    fun setUserFollowed(userId: Int, followed: Boolean) {
        val key = userId.toString()
        if (followed) {
            preferences.edit { putBoolean(key, true) }
        } else {
            preferences.edit { remove(key) }
        }
    }

    private companion object {
        private const val PREFERENCES = "followed-users"
    }
}