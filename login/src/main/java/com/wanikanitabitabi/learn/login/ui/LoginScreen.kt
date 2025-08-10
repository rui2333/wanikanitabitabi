package com.wanikanitabitabi.learn.login.ui

import android.util.Log
import android.webkit.WebView
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.wanikanitabitabi.learn.login.util.WaniTabiAuthManager


@Composable
fun LoginScreen(
    authManager: WaniTabiAuthManager = hiltViewModel(),
    onLoginSuccess: () -> Unit = {}
) {
    val loginState by authManager.loginState.collectAsState()
    
    Log.d(TAG, "LoginScreen recomposed with state: $loginState")
    
    LaunchedEffect(loginState) {
        Log.d(TAG, "LaunchedEffect triggered with state: $loginState")
        if (loginState is WaniTabiAuthManager.LoginState.Success) {
            Log.d(TAG, "Success state detected, calling onLoginSuccess")
            onLoginSuccess()
        }
    }
    
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues()),
        factory = { context ->
            WebView(context).apply {
                authManager.setupWebView(webView = this)
            }
        }
    )
}

private const val TAG = "LoginScreen"