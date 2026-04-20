package com.novoda.test.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FollowedUsersTest {

    private lateinit var context: Context
    private lateinit var followedUsers: FollowedUsers

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        followedUsers = FollowedUsers(context)
    }

    @Test
    fun setUserFollowed() {
        assertThat(followedUsers.isUserFollowed(1)).isFalse()
        assertThat(followedUsers.isUserFollowed(2)).isFalse()
        assertThat(followedUsers.followedIds).isEmpty()

        followedUsers.setUserFollowed(2, true)
        assertThat(followedUsers.isUserFollowed(1)).isFalse()
        assertThat(followedUsers.isUserFollowed(2)).isTrue()
        assertThat(followedUsers.followedIds).containsExactly(2)

        followedUsers.setUserFollowed(1, true)
        assertThat(followedUsers.isUserFollowed(1)).isTrue()
        assertThat(followedUsers.isUserFollowed(2)).isTrue()
        assertThat(followedUsers.followedIds).containsExactly(1, 2)

        followedUsers.setUserFollowed(1, false)
        followedUsers.setUserFollowed(2, false)
        assertThat(followedUsers.isUserFollowed(1)).isFalse()
        assertThat(followedUsers.isUserFollowed(2)).isFalse()
        assertThat(followedUsers.followedIds).isEmpty()
    }

}