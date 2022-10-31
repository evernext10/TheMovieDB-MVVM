package com.appiadev.repository

import com.appiadev.model.api.MovieResponse
import com.appiadev.utils.AppResult

interface UniversalRepository {
    suspend fun getUpcomingMovies(page: Int): AppResult<MovieResponse>
    suspend fun getTrendsMovies(page: Int): AppResult<MovieResponse>
    suspend fun getRecommendedMovies(page: Int): AppResult<MovieResponse>
}
