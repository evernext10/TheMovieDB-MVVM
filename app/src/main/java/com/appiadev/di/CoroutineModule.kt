package com.appiadev.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coroutineModule = module {
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    single { provideIoDispatcher() }
}
