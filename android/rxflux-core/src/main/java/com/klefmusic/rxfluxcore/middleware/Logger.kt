package com.klefmusic.rxfluxcore.middleware

import com.klefmusic.rxfluxcore.ReduxStore
import com.klefmusic.rxfluxcore.withMiddleware
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableSource
import io.reactivex.rxjava3.kotlin.ofType

interface Logger {

    fun log(loggable: String)

}

interface Loggable {
    fun log(): String = javaClass.name
}

fun <S> ReduxStore<S>.withLogger(logger: Logger?): ReduxStore<S> =
    withMiddleware { store ->
        val ignoreElements: CompletableSource =
            store.actions.doOnNext { logger?.log("dispatched: $it state: ${store.currentViewState()} ") }
                .ofType()
                .ignoreElements()
        Completable.mergeArray(
            ignoreElements
        )
    }