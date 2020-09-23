package com.klefmusic.flux.ui.success

import com.klefmusic.flux.ui.success.fragment1.Fragment1EffectMapper
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val successModule = module {
    // select sensor
    factory { Fragment1EffectMapper() }
}