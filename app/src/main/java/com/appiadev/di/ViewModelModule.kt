package com.appiadev.di

import com.appiadev.viewModel.UniversalViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        UniversalViewModel(repository = get())
    }
}
