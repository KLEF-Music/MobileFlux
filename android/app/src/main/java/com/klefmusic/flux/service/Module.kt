package com.klefmusic.flux.service

import org.koin.dsl.module

val serviceModule = module {
    single { Server() }
    factory { UserDataFetcher(get()) }
}