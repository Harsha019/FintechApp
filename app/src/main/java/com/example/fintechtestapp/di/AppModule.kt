package com.example.fintechtestapp.di

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.fintechtestapp.data.local.AppDatabase
import com.example.fintechtestapp.data.local.UserDao
import com.example.fintechtestapp.data.repository.AccessRepository
import com.example.fintechtestapp.ui.viewmodel.MainViewModel
import com.example.fintechtestapp.utils.AccessManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_db"
        ).fallbackToDestructiveMigration().build()
    }

    single<UserDao> { get<AppDatabase>().userDao() }

    single { AccessManager() }

    single { AccessRepository(get()) }

    viewModel { MainViewModel(get(), get()) }
}
