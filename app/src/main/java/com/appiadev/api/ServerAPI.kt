package com.appiadev.api

import com.appiadev.model.api.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPI {
    @GET("movie/popular")
    suspend fun getAllMovies(@Query("page") page: Int): Response<MovieResponse>
}
