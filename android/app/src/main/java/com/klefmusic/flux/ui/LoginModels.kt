package com.klefmusic.flux.ui

import com.klefmusic.flux.validate.EmailValidResult
import com.klefmusic.flux.validate.PasswordValidResult
import com.klefmusic.rxfluxcore.Effect
import com.klefmusic.rxfluxcore.models.State
import com.klefmusic.rxfluxcore.models.UiEvent

sealed class LoginEvents : UiEvent {
    data class EmailChanged(val email: String) : LoginEvents()
    data class PasswordChanged(val password: String) : LoginEvents()
    data class LoginClicked(val email: String, val password: String) : LoginEvents()
}

data class LoginState(
    // Loading state.
    val loading: Boolean = false,
    // Form state.
    val emailValid: EmailValidResult = EmailValidResult.Valid,
    val passwordValid: PasswordValidResult = PasswordValidResult.Valid,
    val email: String = "",
    val password: String = ""
) : State {
    val canSignIn: Boolean =  passwordValid is PasswordValidResult.Valid && emailValid is EmailValidResult.Valid
}

sealed class LoginEffects : Effect {
    object OpenLoggedIn: LoginEffects()
}
