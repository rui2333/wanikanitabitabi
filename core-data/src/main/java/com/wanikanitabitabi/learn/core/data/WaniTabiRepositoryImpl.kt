package com.wanikanitabitabi.learn.core.data

import com.wanikanitabitabi.learn.core.data.data.ApiKeyVerificationResult
import com.wanikanitabitabi.learn.core.data.data.HttpStatus
import com.wanikanitabitabi.learn.core.data.data.UserInfo
import com.wanikanitabitabi.learn.core.data.data.UserInfoResult
import com.wanikanitabitabi.learn.core.database.WaniTabi
import com.wanikanitabitabi.learn.core.database.WaniTabiDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WaniTabiRepositoryImpl @Inject constructor(
    private val waniTabiDao: WaniTabiDao
) : WaniTabiRepository {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()

    override val waniTabis: Flow<List<String>> =
        waniTabiDao.getWaniTabis().map { items -> items.map { it.name } }

    override suspend fun login(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun add(name: String) {
        waniTabiDao.insertWaniTabi(WaniTabi(name = name))
    }

    /**
     * Verifies the provided WaniKani API key by making a request to the user endpoint.
     *
     * This function executes on the IO dispatcher as it involves network operations.
     * It constructs an HTTP GET request to the WaniKani API's user endpoint,
     * including the API key in the Authorization header.
     *
     * The function then processes the HTTP response:
     * - If the response code is OK (200), it attempts to parse the JSON response
     *   to extract the username. If successful, it returns [ApiKeyVerificationResult.Valid]
     *   with the username. If parsing fails or the response body is null, it returns
     *   [ApiKeyVerificationResult.Valid] with an empty string (indicating a valid key but an issue
     *   retrieving the username, which might be treated as a less severe error by the caller).
     * - If the response code is UNAUTHORIZED (401) or FORBIDDEN (403), it returns
     *   [ApiKeyVerificationResult.Invalid].
     * - If the response code is TOO_MANY_REQUESTS (429), it returns
     *   [ApiKeyVerificationResult.RateLimited].
     * - For any other response code, it returns [ApiKeyVerificationResult.NetworkError]
     *   with a message including the unexpected response code.
     *
     * Network-related exceptions (e.g., [IOException]) are caught and result in
     * [ApiKeyVerificationResult.NetworkError] with the error message.
     * Other general exceptions during the process are caught and result in
     * [ApiKeyVerificationResult.Error] with the error message.
     *
     * @param apiKey The WaniKani API key to verify.
     * @return An [ApiKeyVerificationResult] indicating the outcome of the verification.
     *         This can be [ApiKeyVerificationResult.Valid], [ApiKeyVerificationResult.Invalid],
     *         [ApiKeyVerificationResult.RateLimited], [ApiKeyVerificationResult.NetworkError],
     *         or [ApiKeyVerificationResult.Error].
     */
    override suspend fun verifyApiKey(apiKey: String): ApiKeyVerificationResult {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(API_BASE_URL + USER_ENDPOINT)
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Wanikani-Revision", "20170710")
                    .build()

                val response: Response = httpClient
                    .newCall(request)
                    .execute()

                when (response.code) {
                    HttpStatus.OK.code -> {
                        val responseBody = response.body?.string()

                        responseBody?.let {
                            try {
                                val jsonResponse = JSONObject(responseBody)
                                val userData = jsonResponse.getJSONObject("data")
                                val username = userData.getString("username")
                                ApiKeyVerificationResult.Valid(username)
                            } catch (e: Exception) {
                                // no-op
                            }
                        }
                        ApiKeyVerificationResult.Valid("")
                    }
                    HttpStatus.UNAUTHORIZED.code, HttpStatus.FORBIDDEN.code ->
                        ApiKeyVerificationResult.Invalid
                    HttpStatus.TOO_MANY_REQUESTS.code ->
                        ApiKeyVerificationResult.RateLimited
                    else ->
                        ApiKeyVerificationResult.NetworkError("Unexpected response code: ${response.code}")
                }
            } catch (e: IOException) {
                ApiKeyVerificationResult.NetworkError("Network error: ${e.message}")
            } catch (e: Exception) {
                ApiKeyVerificationResult.Error("Error verifying API key: ${e.message}")
            }
        }
    }

    override suspend fun getUserInfo(apiKey: String): UserInfoResult {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(API_BASE_URL + USER_ENDPOINT)
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Wanikani-Revision", "20170710")
                    .build()

                val response: Response = httpClient
                    .newCall(request)
                    .execute()

                when (response.code) {
                    HttpStatus.OK.code -> {
                        val responseBody = response.body?.string()

                        responseBody?.let {
                            try {
                                val jsonResponse = JSONObject(responseBody)
                                val userData = jsonResponse.getJSONObject("data")

                                val userInfo = UserInfo(
                                    username = userData.getString("username"),
                                    level = userData.getInt("level"),
                                    profileUrl = userData.optString("profile_url"),
                                    startedAt = userData.optString("started_at"),
                                    subscriptionActive = userData.optJSONObject("subscription")?.optBoolean("active") ?: false
                                )

                                UserInfoResult.Success(userInfo)
                            } catch (e: Exception) {
                                // no-op
                            }
                        }
                        UserInfoResult.Error("Failed to parse user info")
                    }
                    else ->
                        UserInfoResult.Error("Unexpected response code: ${response.code}")
                }
            } catch (e: IOException) {
                UserInfoResult.Error("Network error: ${e.message}")
            } catch (e: Exception) {
                UserInfoResult.Error("Error verifying API key: ${e.message}")
            }
        }
    }

    companion object {
        private const val TIMEOUT: Long = 30 // seconds
        private const val API_BASE_URL = "https://api.wanikani.com/v2"
        private const val USER_ENDPOINT = "/user"
    }
}