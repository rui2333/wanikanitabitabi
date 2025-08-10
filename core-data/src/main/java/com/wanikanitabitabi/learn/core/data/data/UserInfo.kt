package com.wanikanitabitabi.learn.core.data.data

/**
 * User information data class
 */
data class UserInfo(
    val username: String,
    val level: Int,
    val profileUrl: String,
    val startedAt: String,
    val subscriptionActive: Boolean
)