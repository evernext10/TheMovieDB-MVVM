package com.appiadev.di

import android.app.Application
import androidx.room.Room
import com.appiadev.db.AppDatabase
import com.appiadev.db.daos.MovieDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "db")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideMoviesDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideMoviesDao(get()) }
}
