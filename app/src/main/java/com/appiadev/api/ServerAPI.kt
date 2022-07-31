package com.appiadev.api

import com.appiadev.model.api.MovieResponse
import retrofit2.Response
import retrofit2.http.GET

interface ServerAPI {
    @GET("movie/popular")
    suspend fun getAllMovies(): Response<MovieResponse>
}
