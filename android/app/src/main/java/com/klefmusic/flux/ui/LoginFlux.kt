package com.klefmusic.flux.ui

import com.klefmusic.flux.service.LoginResult
import com.klefmusic.flux.service.UserDataFetcher
import com.klefmusic.flux.validate.EmailValidResult
import com.klefmusic.flux.validate.PasswordValidResult
import com.klefmusic.flux.validate.isEmailValid
import com.klefmusic.flux.validate.isValidPassword
import com.klefmusic.rxfluxcore.*
import com.klefmusic.rxfluxcore.models.Action
import com.klefmusic.rxfluxcore.middleware.Logger
import com.klefmusic.rxfluxcore.middleware.epic
import com.klefmusic.rxfluxcore.middleware.withEpics
import com.klefmusic.rxfluxcore.middleware.withLogger
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.Observables
import io.reactivex.rxjava3.kotlin.merge
import io.reactivex.rxjava3.kotlin.ofType
import org.koin.dsl.module
import timber.log.Timber

class LoginStore(userDataFetcher: UserDataFetcher) : ReduxStore<LoginState> by
createStore<LoginState>(Single.just(LoginState()), loginReducer)
    .withEpics(validateLogin(), performLogin(userDataFetcher))
    .withLogger(object : Logger {
        override fun log(loggable: String) {
            Timber.tag("redux").d(loggable)
        }
    })


fun validateLogin() = epic<LoginState> { actions, _ ->
    // get the events we care about
    val passwordChanged = actions.ofType<LoginEvents.PasswordChanged>()
    val emailChanged = actions.ofType<LoginEvents.EmailChanged>()

    // check that form data is valid
    val isPasswordValid = passwordChanged.map { it.password.isValidPassword() }.share()
    val isEmailValid = emailChanged.map { it.email.isEmailValid() }.share()

    listOf(
        isPasswordValid,
        isEmailValid
    ).merge()
}

fun performLogin(userDataFetcher: UserDataFetcher) = epic<LoginState> { actions, _ ->
    actions.ofType<LoginEvents.LoginClicked>().flatMap { userDataFetcher.login(it.email, it.password) }
}

val loginReducer = reducer<LoginState> { oldState, action ->
    when (action) {
        is LoginResult -> {
            when (action) {
                LoginResult.Loading -> oldState.copy(loading = true)
                is LoginResult.Error -> oldState.copy(loading = false)
                is LoginResult.Success -> oldState.copy(loading = false)
            }
        }
        is EmailValidResult -> {
            oldState.copy(emailValid = action)
        }
        is PasswordValidResult -> {
            oldState.copy(passwordValid = action)
        }
        is LoginEvents.EmailChanged -> oldState.copy(email = action.email)
        is LoginEvents.PasswordChanged -> oldState.copy(password = action.password)
        else -> oldState
    }
}

class LoginEffectMapper : EffectMapper<LoginEffects, Action> {
    override fun mapToEffects(result: Observable<Action>): Observable<LoginEffects> {
        return result.ofType<LoginResult.Success>().map { LoginEffects.OpenLoggedIn }
    }
}

val mainActivityModule = module {
    single { LoginStore(get()) }
    factory { LoginEffectMapper() }
}

