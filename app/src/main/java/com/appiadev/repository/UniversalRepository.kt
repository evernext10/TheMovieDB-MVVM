package com.appiadev.repository

import androidx.paging.PagingData
import com.appiadev.model.api.MovieResponse
import com.appiadev.utils.AppResult

interface UniversalRepository {
    suspend fun getAllMovies(): AppResult<PagingData<MovieResponse>>
}
