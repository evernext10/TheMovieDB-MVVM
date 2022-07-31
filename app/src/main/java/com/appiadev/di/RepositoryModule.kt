package com.appiadev.di

import android.content.Context
import com.appiadev.api.ServerAPI
import com.appiadev.db.daos.MovieDao
import com.appiadev.repository.UniversalRepository
import com.appiadev.repository.UniversalRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideCountryRepository(api: ServerAPI, context: Context, movieDao: MovieDao): UniversalRepository {
        return UniversalRepositoryImpl(api, context, movieDao)
    }

    single { provideCountryRepository(get(), androidContext(), get()) }
}
