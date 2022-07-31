package com.appiadev.di

import android.app.Application
import androidx.room.Room
import com.appiadev.db.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "db")
            .fallbackToDestructiveMigration()
            .build()
    }

    /*fun provideCountriesDao(database: AppDatabase): CountriesDao {
        return  database.countriesDao
    }*/

    single { provideDatabase(androidApplication()) }
    // single { provideCountriesDao(get()) }
}
