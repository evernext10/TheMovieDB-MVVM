package com.appiadev.ui.home.navigation.list.repository

import com.appiadev.model.api.MovieResponse
import com.appiadev.utils.AppResult

interface MovieListRepository {
    suspend fun getUpcomingMovies(page: Int): AppResult<MovieResponse>
    suspend fun getTrendsMovies(page: Int): AppResult<MovieResponse>
    suspend fun getRecommendedMovies(
        filterType: String,
        page: Int
    ): AppResult<MovieResponse>
}
