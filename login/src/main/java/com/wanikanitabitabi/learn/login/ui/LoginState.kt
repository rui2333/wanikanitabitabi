package com.wanikanitabitabi.learn.login.ui


/**
 * Represents the state of the login screen.
 *
 * @property email The current email input value.
 * @property password The current password input value.
 * @property isLoading Indicates whether a login operation is in progress.
 * @property isPasswordVisible Controls the visibility of the password input.
 * @property rememberMe Indicates whether the user wants to be remembered.
 * @property errorMessage An optional error message to display to the user.
 * @property isEmailValid Indicates whether the entered email is considered valid.
 * @property isPasswordValid Indicates whether the entered password is considered valid.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isPasswordVisible: Boolean = false,
    val rememberMe: Boolean = false,
    val errorMessage: String? = null,
    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true
)