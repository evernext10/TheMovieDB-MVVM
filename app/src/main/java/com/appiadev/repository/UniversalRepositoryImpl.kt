package com.appiadev.repository

import android.content.Context
import android.util.Log
import com.appiadev.api.ServerAPI
import com.appiadev.db.daos.MovieDao
import com.appiadev.model.api.MovieResponse
import com.appiadev.model.api.Movie
import com.appiadev.utils.AppResult
import com.appiadev.utils.NetworkManager.isOnline
import com.appiadev.utils.handleApiError
import com.appiadev.utils.handleSuccess
import com.appiadev.utils.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UniversalRepositoryImpl(
    private val api: ServerAPI,
    private val context: Context,
    private val movieDao: MovieDao
) : UniversalRepository {

    override suspend fun getAllMovies(page: Int): AppResult<MovieResponse> {
        val hasInternet = isOnline(context)
        if (hasInternet) {
            return try {
                val response = api.getAllMovies(page)
                if (response.isSuccessful) {
                    // save the data
                    response.body()?.let {
                        withContext(Dispatchers.IO) {
                            it.movieResults?.let { data ->
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
            val data = getMoviesDataFromCache()
            return if (data.isNotEmpty()) {
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

    private suspend fun getMoviesDataFromCache(): List<Movie> {
        return withContext(Dispatchers.IO) {
            movieDao.findAll()
        }
    }
}
