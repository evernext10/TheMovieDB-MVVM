package com.appiadev.di

import com.appiadev.ui.home.navigation.detail.viewModel.MovieDetailViewModel
import com.appiadev.ui.home.navigation.list.viewModel.MovieListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MovieListViewModel(repository = get())
    }
    viewModel {
        MovieDetailViewModel(repository = get())
    }
}
