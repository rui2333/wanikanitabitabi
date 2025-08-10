package com.wanikanitabitabi.learn.login.ui

sealed class LoginEvent {
    /**
     * Represents the event when the email input field is changed.
     *
     * @property email The new email value.
     */
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object PasswordVisibilityToggled : LoginEvent()
    object RememberMeToggled : LoginEvent()
    object LoginClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object SignUpClicked : LoginEvent()
    object GoogleSignInClicked : LoginEvent()
    object ErrorDismissed : LoginEvent()
}