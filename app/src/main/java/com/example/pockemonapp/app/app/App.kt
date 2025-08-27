package com.example.pockemonapp.app.app

import android.app.Application
import com.example.pockemonapp.app.di.appModule
import com.example.pockemonapp.app.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule, networkModule)
        }
    }
}