package com.wanikanitabitabi.learn.core.data.data

/**
 * Result types for user info retrieval
 */
sealed class UserInfoResult {
    data class Success(val userInfo: UserInfo) : UserInfoResult()
    object NoApiKey : UserInfoResult()
    data class Error(val message: String) : UserInfoResult()
}