package com.klefmusic.flux.ui.success.fragment1

import com.klefmusic.rxfluxcore.EffectMapper
import com.klefmusic.rxfluxcore.models.Action
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.ofType

class Fragment1EffectMapper : EffectMapper<Fragment1Effects, Action> {

    override fun mapToEffects(result: Observable<Action>): Observable<Fragment1Effects> =
        result.ofType<Fragment1Events.clickedOpen>().map { Fragment1Effects.openFragment2 }

}