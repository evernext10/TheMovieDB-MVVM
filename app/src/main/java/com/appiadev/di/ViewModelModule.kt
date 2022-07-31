package com.appiadev.di

import org.koin.dsl.module

val viewModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    /*viewModel {
        CountriesViewModel(repository = get())
    }*/
}
