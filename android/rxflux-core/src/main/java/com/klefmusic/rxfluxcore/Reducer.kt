package com.klefmusic.rxfluxcore

import com.klefmusic.rxfluxcore.models.Action

/**
 * A function that returns the next state tree, given
 * the current state tree and the action to handle.
 */
typealias Reducer<S> = (oldState: S, action: Action) -> S

fun <S> reducer(wrapped: (state: S, action: Action) -> S): Reducer<S> = wrapped

fun <S> combineReducers(reducers: List<Reducer<S>>): Reducer<S> =
    reducer { state, action -> reducers.fold(state) { acc, r -> r(acc, action) } }

