package com.novoda.test.model

data class User(
    val id: Int,
    val name: String,
    val reputation: Int,
    val imageUrl: String,
    val followed: Boolean
)