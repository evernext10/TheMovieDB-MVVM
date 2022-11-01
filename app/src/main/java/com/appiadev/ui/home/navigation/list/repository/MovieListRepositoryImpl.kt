package com.appiadev.ui.home.navigation.list.repository

import android.content.Context
import android.util.Log
import com.appiadev.api.ServerAPI
import com.appiadev.db.daos.MovieDao
import com.appiadev.model.api.Movie
import com.appiadev.model.api.MovieResponse
import com.appiadev.model.core.MovieType
import com.appiadev.utils.AppResult
import com.appiadev.utils.NetworkManager.isOnline
import com.appiadev.utils.handleApiError
import com.appiadev.utils.handleSuccess
import com.appiadev.utils.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieListRepositoryImpl(
    private val api: ServerAPI,
    private val context: Context,
    private val movieDao: MovieDao
) : MovieListRepository {

    private suspend fun getMoviesDataFromCache(type: String): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieDao.findAllMoviesByType(type)
        }
    }

    override suspend fun getUpcomingMovies(page: Int): AppResult<MovieResponse> {
        return fetchMoviesByType(MovieType.Upcoming, page)
    }

    override suspend fun getTrendsMovies(page: Int): AppResult<MovieResponse> {
        return fetchMoviesByType(MovieType.Trends, page)
    }

    override suspend fun getRecommendedMovies(
        filterType: String,
        page: Int
    ): AppResult<MovieResponse> {
        return fetchMoviesByType(MovieType.Recommended, page, filterType)
    }

    private suspend fun fetchMoviesByType(type: MovieType, page: Int, filterType: String? = null): AppResult<MovieResponse> {
        val hasInternet = isOnline(context)
        if (hasInternet) {
            return try {
                val response = when (type) {
                    is MovieType.Upcoming -> api.getUpcoming(page)
                    is MovieType.Trends -> api.getTrends(page)
                    is MovieType.Recommended -> when (filterType) {
                        "language" -> api.getRecommendedByLanguage(page)
                        "year" -> api.getRecommendedByYear(page)
                        else -> api.getRecommended(page)
                    }
                }

                if (response.isSuccessful) {
                    // save the data
                    response.body()?.let {
                        withContext(Dispatchers.IO) {
                            it.movieResults?.let { data ->
                                data.forEach { it.type = type::class.simpleName }

                                movieDao.insertMovieList(data)
                                Log.d("DB", "Saved")
                            }
                        }
                    }
                    handleSuccess(response)
                } else {
                    handleApiError(response)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            // check in db if the data exists
            val data = type::class.simpleName?.let { getMoviesDataFromCache(it) }
            return if (data?.isNotEmpty() == true) {
                Log.d("DB", "from db")
                val result = MovieResponse(
                    movieResults = data
                )
                AppResult.Success(result)
            } else {
                // no network
                context.noNetworkConnectivityError()
            }
        }
    }
}
