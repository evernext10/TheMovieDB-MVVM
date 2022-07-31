package com.appiadev.fakes

import com.appiadev.TestData
import com.appiadev.model.api.MovieResponse
import com.appiadev.repository.UniversalRepository
import com.appiadev.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeMovieRepository : UniversalRepository {

    private var isToThrowException: Boolean = false

    fun setThrowException(isToThrowException: Boolean) {
        this.isToThrowException = isToThrowException
    }

    override suspend fun getAllMovies(): AppResult<MovieResponse> {
        if (isToThrowException) throw Exception("Network Error")
        val apiResponse = TestData.movieApiModel1

        return if (apiResponse.movieResults!!.isNotEmpty()) {
            // save the data
            apiResponse.let {
                withContext(Dispatchers.IO) {
                    it.movieResults?.let { data -> }
                }
            }
            AppResult.Success(apiResponse)
        } else {
            AppResult.Error(Exception("No data Error"))
        }
    }
}
