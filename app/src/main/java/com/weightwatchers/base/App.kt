package com.weightwatchers.base

import android.app.Application
import com.weightwatchers.di.ApplicationModule.module
import com.weightwatchers.utils.StaticResourcesProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        lateinit var instance: App
    }


    override fun onCreate() {
        super.onCreate()

        instance = this

        StaticResourcesProvider.init(this)

        startKoin {
            androidContext(applicationContext)
            modules(module)
        }
    }
}


