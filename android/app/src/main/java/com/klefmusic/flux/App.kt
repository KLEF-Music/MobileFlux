package com.klefmusic.flux

import android.app.Application
import com.klefmusic.flux.service.serviceModule
import com.klefmusic.flux.ui.mainActivityModule
import com.klefmusic.flux.ui.success.successModule
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                listOf(
                    mainActivityModule,
                    serviceModule,
                    successModule
                )
            )
        }

        Timber.plant(Timber.DebugTree())
    }
}