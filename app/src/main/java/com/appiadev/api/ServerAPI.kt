package com.appiadev.api

import com.appiadev.model.api.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPI {
    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getTrends(@Query("page") page: Int): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getRecommended(
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getRecommendedByLanguage(
        @Query("page") page: Int,
        @Query("language") language: String = "es"
    ): Response<MovieResponse>

    @GET("movie/top_rated")
    suspend fun getRecommendedByYear(
        @Query("page") page: Int,
        @Query("primary_release_date.gte") yearStart: String = "2021-01-01",
        @Query("primary_release_date.lte") yearEnd: String = "2021-12-31"
    ): Response<MovieResponse>
}
