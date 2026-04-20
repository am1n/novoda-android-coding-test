package com.novoda.test.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserJson(
    @field:Json(name = "user_id") val userId: Int,
    @field:Json(name = "display_name") val displayName: String,
    @field:Json(name = "reputation") val reputation: Int,
    @field:Json(name = "profile_image") val profileImage: String
)

@JsonClass(generateAdapter = true)
data class UserResultsJson(
    @field:Json(name = "items") val items: List<UserJson>
)
