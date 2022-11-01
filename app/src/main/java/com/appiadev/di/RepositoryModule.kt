package com.appiadev.di

import android.content.Context
import com.appiadev.api.ServerAPI
import com.appiadev.db.daos.MovieDao
import com.appiadev.ui.home.navigation.detail.repository.MovieDetailRepository
import com.appiadev.ui.home.navigation.detail.repository.MovieDetailRepositoryImpl
import com.appiadev.ui.home.navigation.list.repository.MovieListRepository
import com.appiadev.ui.home.navigation.list.repository.MovieListRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideMovieRepository(api: ServerAPI, context: Context, movieDao: MovieDao): MovieListRepository {
        return MovieListRepositoryImpl(api, context, movieDao)
    }

    single { provideMovieRepository(get(), androidContext(), get()) }

    fun provideMovieDetailRepository(api: ServerAPI, context: Context, movieDao: MovieDao): MovieDetailRepository {
        return MovieDetailRepositoryImpl(api, context, movieDao)
    }

    single { provideMovieDetailRepository(get(), androidContext(), get()) }
}
