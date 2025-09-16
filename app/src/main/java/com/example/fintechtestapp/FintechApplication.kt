package com.example.fintechtestapp

import android.app.Application
import com.example.fintechtestapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FintechApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FintechApplication)
            modules(appModule)
        }
    }
}
