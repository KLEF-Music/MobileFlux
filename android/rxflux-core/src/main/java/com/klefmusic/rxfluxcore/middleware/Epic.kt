package com.klefmusic.rxfluxcore.middleware

import com.klefmusic.rxfluxcore.ReduxStore
import com.klefmusic.rxfluxcore.models.Action
import com.klefmusic.rxfluxcore.withMiddleware
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.merge

typealias Epic<S> = (actions: Observable<Action>, state: () -> S) -> Observable<Action>

fun <S> epic(epic: Epic<S>): Epic<S> = epic
fun <S> emptyEpic(): Epic<S> = epic { _, _ -> Observable.empty() }

fun <S> combineEpics(vararg epic: Epic<S>): Epic<S> =
    epic { actions, state -> epic.map { it(actions, state) }.merge() }

fun <S> ReduxStore<S>.withEpics(vararg epics: Epic<S>): ReduxStore<S> =
    withEpics(combineEpics(*epics))

fun <S> ReduxStore<S>.withEpics(epics: Epic<S>): ReduxStore<S> =
    withMiddleware { store ->
        epics(store.actions, store::currentViewState).doOnNext { store.dispatch(it) }
            .ignoreElements()
    }
