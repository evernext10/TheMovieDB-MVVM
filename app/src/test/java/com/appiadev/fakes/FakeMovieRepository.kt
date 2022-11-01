package com.appiadev.fakes

import com.appiadev.TestData
import com.appiadev.model.api.MovieResponse
import com.appiadev.ui.home.navigation.list.repository.MovieListRepository
import com.appiadev.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FakeMovieRepository : MovieListRepository {

    private var isToThrowException: Boolean = false

    fun setThrowException(isToThrowException: Boolean) {
        this.isToThrowException = isToThrowException
    }

    override suspend fun getUpcomingMovies(page: Int): AppResult<MovieResponse> {
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

    override suspend fun getTrendsMovies(page: Int): AppResult<MovieResponse> {
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

    override suspend fun getRecommendedMovies(
        filterType: String,
        page: Int
    ): AppResult<MovieResponse> {
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
