package com.appiadev.di

import org.koin.dsl.module

val repositoryModule = module {

    /*fun provideCountryRepository(api: ServerAPI, context: Context, dao : CountriesDao): CountriesRepository {
        return CountriesRepositoryImpl(api, context, dao)
    }

    single { provideCountryRepository(get(), androidContext(), get()) }

     */
}
