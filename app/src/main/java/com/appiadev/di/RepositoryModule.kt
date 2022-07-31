package com.appiadev.di

import android.content.Context
import com.appiadev.api.ServerAPI
import com.appiadev.repository.UniversalRepository
import com.appiadev.repository.UniversalRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideCountryRepository(api: ServerAPI, context: Context): UniversalRepository {
        return UniversalRepositoryImpl(api, context)
    }

    single { provideCountryRepository(get(), androidContext()) }
}
