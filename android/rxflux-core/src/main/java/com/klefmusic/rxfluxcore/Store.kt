package com.klefmusic.rxfluxcore

import com.jakewharton.rx3.replayingShare
import com.jakewharton.rxrelay3.PublishRelay
import com.klefmusic.rxfluxcore.models.Action
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single


interface ReduxStore<S> {

    fun dispatch(action: Action)

    val actions: Observable<Action>

    fun currentViewState(): S

    val updates: Observable<S>

}

/**
 * Simplest possible redux implementation
 */
fun <S> Observable<Action>.reduxStore(
    initialStateSupplier: Single<S>,
    reducer: Reducer<S>
): @NonNull Observable<S> =
    initialStateSupplier
        .flatMapObservable { initialState ->
            scan(initialState) { oldState, result ->
                reducer(oldState, result)
            }
        }
        .distinctUntilChanged()
        .replayingShare()

fun <S> createStore(
    initialStateSupplier: Single<S>,
    reducer: Reducer<S>
): ReduxStore<S> = object : ReduxStore<S> {

    private val resultsRelay = PublishRelay.create<Action>()

    override val actions: Observable<Action> = resultsRelay.hide()

    override fun dispatch(action: Action) = resultsRelay.accept(action)

    override fun currentViewState(): S = updates.blockingFirst()

    override val updates: Observable<S> = actions.reduxStore(initialStateSupplier, reducer)
}

fun <S> ReduxStore<S>.withMiddleware(middleware: (store: ReduxStore<S>) -> Completable): ReduxStore<S> {
    val wrapped = this
    val middlewareCompletable = middleware(wrapped)
    return object : ReduxStore<S> {

        override val actions: Observable<Action> = wrapped.actions

        override fun dispatch(action: Action) = wrapped.dispatch(action)

        override val updates: Observable<S> =
            Observable.merge(
                wrapped.updates,
                middlewareCompletable.toObservable()
            )

        override fun currentViewState(): S = wrapped.currentViewState()
    }
}
