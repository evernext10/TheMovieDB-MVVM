package com.appiadev.api

import retrofit2.Response
import retrofit2.http.GET

interface ServerAPI {
    @GET("/api/v1")
    suspend fun getAllCountries(): Response<List<String>>
}
