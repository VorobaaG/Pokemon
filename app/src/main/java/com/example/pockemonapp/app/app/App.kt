package com.example.pockemonapp.app.app

import android.app.Application
import com.example.pockemonapp.app.di.appModule
import com.example.pockemonapp.app.di.networkModule
import com.example.pockemonapp.app.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App:Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, networkModule, roomModule)
        }
    }
}