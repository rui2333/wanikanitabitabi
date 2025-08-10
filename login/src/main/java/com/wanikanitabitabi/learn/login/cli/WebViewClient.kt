package com.wanikanitabitabi.learn.login.cli

import android.content.Context
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WaniTabiWebViewClient(
    private val context: Context,
    private val onSuccess: (String) -> Unit,
    private val onError: (String) -> Unit
): WebViewClient() {
    
    override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
        Log.d(TAG, "doUpdateVisitedHistory: $url (isReload: $isReload)")
        
        // Check if we navigated from login page to a successful login page
        if (!isReload && url != null && url.isSuccessfullyLoggedIn()) {
            val history = view?.copyBackForwardList()
            val previousItem = history?.getItemAtIndex(history.currentIndex - 1)
            val previousUrl = previousItem?.url
            
            Log.d(TAG, "Previous URL: $previousUrl")
            
            if (previousUrl?.contains("login") == true) {
                Log.d(TAG, "Detected redirect from login, navigating to TOKEN_URL")
                view.loadUrl(TOKEN_URL)
            }
        }
        
        super.doUpdateVisitedHistory(view, url, isReload)
    }
    
    override fun onPageFinished(view: WebView, url: String) {
        Log.d(TAG, "onPageFinished: $url")
        
        if (url.isSuccessfullyLoggedIn()) {
            Log.d(TAG, "Successfully logged in detected, redirecting to TOKEN_URL")
            view.loadUrl(TOKEN_URL)
        } else if (url.isSettingsPage()) {
            Log.d(TAG, "On settings page, extracting API key")
            extractApiKeyFromPage(context, view, onSuccess, onError)
        } else {
            Log.d(TAG, "Page finished but no action needed for: $url")
        }
        
        super.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view ?: return false
        val url = request?.url.toString()
        
        if (url.isSuccessfullyLoggedIn()) {
            view.loadUrl(TOKEN_URL)
            return true
        } else if (url.isSettingsPage()) {
            extractApiKeyFromPage(context, view, onSuccess, onError)
            return true
        }
        
        return false
    }

    private fun extractApiKeyFromPage(
        context: Context,
        webView: WebView,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d(TAG, "Starting API key extraction")
        
        // First, let's debug what's on the page
        webView.evaluateJavascript("document.body.innerText") { pageText ->
            Log.d(TAG, "Page content: ${pageText?.take(200)}...")
        }
        
        webView.evaluateJavascript("document.querySelectorAll('input').length") { inputCount ->
            Log.d(TAG, "Number of input elements: $inputCount")
        }
        
        // Now run our API key extraction script
        val javaScript = readJavaScriptFromFile(context).trimIndent()
        Log.d(TAG, "JavaScript to execute: ${javaScript.take(100)}...")
        
        webView.evaluateJavascript(javaScript) { result ->
            Log.d(TAG, "JavaScript result: $result")
            val apiKey = result?.replace("\"", "")?.trim()

            apiKey?.let {
                if (it.isNotBlank()) {
                    Log.d(TAG, "API key extracted successfully: ${it.take(8)}...")
                    onSuccess(it)
                } else {
                    Log.e(TAG, "API key is blank or empty")
                    onError("API key not found")
                }
            } ?: run {
                Log.e(TAG, "API key is null")
                onError("API key not found")
            }
        }
    }

    private fun String.isSuccessfullyLoggedIn(): Boolean {
        return this.startsWith("https://www.wanikani.com/") && 
               !this.contains("login") && 
               !this.contains("settings/personal_access_tokens")
    }

    private fun String.isSettingsPage(): Boolean =
        this.contains(TOKEN)

    private fun readJavaScriptFromFile(context: Context): String {
        return try {
            context.assets.open(JS_SCRIPT_FILE).bufferedReader().use {
                it.readText()
            }
        } catch (e: Exception) {
            ""
        }
    }


    companion object {
        private const val TAG = "WaniTabiWebViewClient"
        private const val MAIN_URL = "https://www.wanikani.com/"
        private const val TOKEN = "settings/personal_access_tokens"
        private const val JS_SCRIPT_FILE = "get_api_key.js"
        private const val TOKEN_URL = "https://www.wanikani.com/settings/personal_access_tokens"
        private const val LOGIN_URL = "https://www.wanikani.com/login"
    }
}