package com.wanikanitabitabi.learn.login.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.wanikanitabitabi.learn.core.data.WaniTabiRepository
import com.wanikanitabitabi.learn.core.data.data.ApiKeyVerificationResult
import com.wanikanitabitabi.learn.login.cli.WaniTabiWebViewClient
import com.wanikanitabitabi.learn.login.util.WaniTabiAuthManager.Constants.LOGIN_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaniTabiAuthManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val waniTabiRepository: WaniTabiRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Loading)
    val loginState: StateFlow<LoginState> = _loginState

    sealed class LoginState {
        data object Loading : LoginState()
        data object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val encryptedPrefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            Constants.PREF_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun setupWebView(webView: WebView) {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_DEFAULT
        }

        webView.webViewClient = WaniTabiWebViewClient(
            context,
            onSuccess = { apiKey ->
                Log.d(TAG, "WebViewClient onSuccess called with API key: ${apiKey.take(8)}...")
                verifyAndCacheApiKey(apiKey)
            },
            onError = { error ->
                Log.e(TAG, "WebViewClient onError called: $error")
                _loginState.value = LoginState.Error(error)
            }
        )

        // Clear previous session
        CookieManager.getInstance().removeAllCookies(null)
        webView.clearCache(true)
        webView.clearHistory()

        // Load login page
        webView.loadUrl(LOGIN_URL)
    }

    private fun verifyAndCacheApiKey(apiKey: String) {
        Log.d(TAG, "Starting API key verification")
        viewModelScope.launch {
            try {
                Log.d(TAG, "Calling waniTabiRepository.verifyApiKey")
                val result = waniTabiRepository.verifyApiKey(apiKey)
                Log.d(TAG, "Verification result: $result")
                
                if (result is ApiKeyVerificationResult.Valid) {
                    Log.d(TAG, "API key is valid, caching and setting success state")
                    cacheApiKey(apiKey)
                    _loginState.value = LoginState.Success
                    Log.d(TAG, "LoginState.Success set")
                } else {
                    Log.e(TAG, "API key is invalid: $result")
                    _loginState.value = LoginState.Error("Invalid API key")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during API key verification", e)
                _loginState.value = LoginState.Error("Failed to verify API key: ${e.message}")
            }
        }
    }

    private fun cacheApiKey(apiKey: String) {
        encryptedPrefs.edit { putString(Constants.API_KEY_PREF, apiKey) }
    }

    private fun getApiKey(): String? {
        return encryptedPrefs.getString(Constants.API_KEY_PREF, null)
    }

    private fun clearApiKey() {
        encryptedPrefs.edit { remove(Constants.API_KEY_PREF) }
    }

    private object Constants {
        const val TIMEOUT: Long = 30 // seconds
        const val PREF_FILE_NAME = "wanitabi_auth_prefs"

        const val API_KEY_PREF = "wanikani_api_key"
        const val API_BASE_URL = "https://api.wanikani.com/v2"
        const val LOGIN_URL = "https://www.wanikani.com/login"
        const val ACCOUNT_URL = "https://www.wanikani.com/settings/account"
    }
    
    companion object {
        private const val TAG = "WaniTabiAuthManager"
    }
}