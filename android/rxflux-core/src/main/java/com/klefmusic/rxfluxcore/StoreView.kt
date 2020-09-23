package com.klefmusic.rxfluxcore

import com.klefmusic.rxfluxcore.models.Action
import com.klefmusic.rxfluxcore.models.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.cast
import io.reactivex.rxjava3.kotlin.merge

interface Effect

interface StoreView<S : State, A: Action, E : Effect> {

    /**
     * Store for the entire application generally injected.
     */
    val store: ReduxStore<S>

    /**
     * A flow that represents updates for this screen. Generally a subsection of the app state.
     */
    val flow: Observable<S>

    /**
     * Events from the UI.
     */
    fun events(): Observable<A> = Observable.empty()

    fun effectMapper() : EffectMapper<E, Action> = emptyEffectMapper()

    fun dispatch(action: A) = store.dispatch(action)

    fun connect(scheduler: Scheduler): Disposable =
        listOf(
            flow.observeOn(scheduler).doOnNext { render(it) }.cast<Any>(),
            events().doOnNext { dispatch(it) }.cast<Any>(),
            effectMapper().mapToEffects(store.actions).observeOn(scheduler).doOnNext { effects(it) }.cast<Any>()
        ).merge().subscribe()

    fun render(viewState: S)

    fun effects(effects: E) {}

}

interface EffectMapper<E : Effect, A: Action> {

    fun mapToEffects(result: Observable<A>): Observable<E> = Observable.empty()
}

fun <E: Effect, A: Action> emptyEffectMapper() = object : EffectMapper<E, A> {

    override fun mapToEffects(result: Observable<A>): Observable<E> {
        return Observable.empty()
    }
}


