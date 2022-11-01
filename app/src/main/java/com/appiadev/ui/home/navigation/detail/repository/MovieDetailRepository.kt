package com.appiadev.ui.home.navigation.detail.repository

import com.appiadev.model.api.Movie
import com.appiadev.model.api.MovieResponse
import com.appiadev.model.api.MovieVideosResponse
import com.appiadev.utils.AppResult

interface MovieDetailRepository {
    suspend fun getMovieDetail(id: Int): AppResult<Movie>
    suspend fun getMovieTrailer(id: Int): AppResult<MovieVideosResponse>
}
