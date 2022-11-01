package com.appiadev.ui.home.navigation.detail.repository

import android.content.Context
import android.util.Log
import com.appiadev.api.ServerAPI
import com.appiadev.db.daos.MovieDao
import com.appiadev.model.api.Movie
import com.appiadev.model.api.MovieResponse
import com.appiadev.utils.AppResult
import com.appiadev.utils.NetworkManager.isOnline
import com.appiadev.utils.handleApiError
import com.appiadev.utils.handleSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieDetailRepositoryImpl(
    private val api: ServerAPI,
    private val context: Context,
    private val movieDao: MovieDao
) : MovieDetailRepository {

    private suspend fun getMovieFromDatabase(id: Int): Movie {
        return withContext(Dispatchers.IO) {
            movieDao.getMovie(id)
        }
    }

    override suspend fun getMovieDetail(id: Int): AppResult<Movie> {
        val hasInternet = isOnline(context)
        if (hasInternet) {
            return try {
                val response = api.getMovieDetail(id.toString())

                if (response.isSuccessful) {
                    // save the data
                    response.body()?.let {
                        withContext(Dispatchers.IO) {
                            // Save on DB
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
            val data = getMovieFromDatabase(id)
            Log.d("DB", "from db")
            return AppResult.Success(data)
        }
    }

    override suspend fun getMovieTrailer(id: Int): AppResult<MovieResponse> {
        TODO("Not yet implemented")
    }
}
