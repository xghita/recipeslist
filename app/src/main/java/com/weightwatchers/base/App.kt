package com.weightwatchers.base

import android.app.Application
import com.weightwatchers.di.ApplicationModule.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        lateinit var instance: App
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        startKoin {
            androidContext(applicationContext)
            modules(module)
        }
    }
}


