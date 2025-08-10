package com.wanikanitabitabi.learn.core.data.data

/**
 * Result types for API key verification
 */
sealed class ApiKeyVerificationResult {
    data class Valid(val username: String) : ApiKeyVerificationResult()
    object Invalid : ApiKeyVerificationResult()
    object NoApiKey : ApiKeyVerificationResult()
    object RateLimited : ApiKeyVerificationResult()
    data class NetworkError(val message: String) : ApiKeyVerificationResult()
    data class Error(val message: String) : ApiKeyVerificationResult()
}