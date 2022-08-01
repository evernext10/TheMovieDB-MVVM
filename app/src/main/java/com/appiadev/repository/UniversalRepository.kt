package com.appiadev.repository

import com.appiadev.model.api.MovieResponse
import com.appiadev.utils.AppResult

interface UniversalRepository {
    suspend fun getAllMovies(page: Int): AppResult<MovieResponse>
}
