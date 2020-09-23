package com.klefmusic.flux.service

import com.klefmusic.rxfluxcore.models.Action
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber

sealed class LoginResult : Action {
    object Loading : LoginResult()
    data class Error(val error: String) : LoginResult()
    data class Success(val user: User) : LoginResult()
}

class UserDataFetcher(private val server: Server) {

    fun login(email :String, password: String): Observable<LoginResult> =
        server.login(email, password)
            .map<LoginResult> {
                LoginResult.Success(it)
            }.onErrorReturn {
                Timber.e(it,"error loading User")
                LoginResult.Error(it.message.orEmpty())
            }
            .toObservable()
            .startWithItem(LoginResult.Loading)
}