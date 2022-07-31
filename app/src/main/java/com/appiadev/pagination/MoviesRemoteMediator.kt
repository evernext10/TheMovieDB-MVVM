package com.appiadev.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.appiadev.api.ServerAPI
import com.appiadev.db.AppDatabase
import com.appiadev.model.api.Movie
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val database: AppDatabase,
    private val api: ServerAPI
) : RemoteMediator<Int, Movie>() {

    val moviesDao = database.movieDao()

    // The page that a movie ID belongs to
    private val keysTable = mutableMapOf<Int, RemoteKeys>()
    class RemoteKeys(val previousKey: Int?, val nextKey: Int?)

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        var numOfItems = 0
        state.pages.forEach {
            numOfItems += it.data.count()
        }

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for prepend.
                val prevKey = remoteKeys?.previousKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val movies = fetchPage(page)!!
            val endOfPaginationReached = movies.isEmpty()
            database.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
//                    database.remoteKeysDao().clearRemoteKeys()
                    keysTable.clear()
                    moviesDao.clearAllPopularMovies()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map {
                    val remoteKeys = RemoteKeys(previousKey = prevKey, nextKey = nextKey)
                    keysTable[it.id!!] = remoteKeys
                }
//                database.remoteKeysDao().insertAll(keys)
                moviesDao.insertMovieList(movies)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun fetchPage(page: Int): List<Movie>? {
        return api.getAllMovies(page).body()?.movieResults
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                // Get the remote keys of the last item retrieved
//                repoDatabase.remoteKeysDao().remoteKeysRepoId(repo.id)
                keysTable[movie.id]
            }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Movie>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                keysTable[repo.id]
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
//                repoDatabase.remoteKeysDao().remoteKeysRepoId(repoId)
                keysTable[id]
            }
        }
    }
}
