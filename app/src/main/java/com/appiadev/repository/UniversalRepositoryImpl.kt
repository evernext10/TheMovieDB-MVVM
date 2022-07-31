package com.appiadev.repository

import android.content.Context
import android.util.Log
import androidx.paging.*
import com.appiadev.api.ServerAPI
import com.appiadev.db.AppDatabase
import com.appiadev.db.daos.MovieDao
import com.appiadev.model.api.MovieResponse
import com.appiadev.model.api.Movie
import com.appiadev.pagination.MoviesRemoteMediator
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
    private val database: AppDatabase
) : UniversalRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getAllMovies(): AppResult<PagingData<MovieResponse>>{
        val hasInternet = isOnline(context)
        if (hasInternet) {
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                remoteMediator = MoviesRemoteMediator(database = database, api),
                pagingSourceFactory = {
                    database.movieDao().moviesPagingSource()
                }
            )
            AppResult.Success(pager)

        } else {
            // check in db if the data exists
            val data = getMoviesDataFromCache()
            return if (data.isNotEmpty()) {
                Log.d("DB", "from db")
                AppResult.Success(MovieResponse(
                    movieResults = data
                ))
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
