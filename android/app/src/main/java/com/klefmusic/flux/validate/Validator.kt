package com.klefmusic.flux.validate

import com.klefmusic.rxfluxcore.models.Action

sealed class EmailValidResult : Action {
    object Valid : EmailValidResult()
    object TooShort : EmailValidResult()
    object BadlyFormatted : EmailValidResult()
}

fun String.isEmailValid(): EmailValidResult =
    when {
        !contains("@") -> EmailValidResult.BadlyFormatted
        !contains(".com") -> EmailValidResult.BadlyFormatted
        length < 4 -> EmailValidResult.TooShort
        else -> EmailValidResult.Valid
    }


sealed class PasswordValidResult : Action {
    object Valid : PasswordValidResult()
    object TooShort : PasswordValidResult()
}

fun String.isValidPassword() = when {
    length < 4 -> PasswordValidResult.TooShort
    else -> PasswordValidResult.Valid
}
