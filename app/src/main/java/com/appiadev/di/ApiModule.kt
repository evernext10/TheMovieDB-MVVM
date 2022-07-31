package com.appiadev.di

import com.appiadev.api.ServerAPI
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    fun provideCountriesApi(retrofit: Retrofit): ServerAPI {
        return retrofit.create(ServerAPI::class.java)
    }
    single { provideCountriesApi(get()) }
}
