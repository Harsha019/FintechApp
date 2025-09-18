package com.example.fintechtestapp

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.fintechtestapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FintechApplication : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FintechApplication)
            modules(appModule)
        }
    }
}
