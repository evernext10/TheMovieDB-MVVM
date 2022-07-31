package com.appiadev.db.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.appiadev.model.api.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie)

    @Query("SELECT * FROM MOVIE WHERE id = :id_")
    fun getMovie(id_: Int): Movie

    @Query("SELECT * FROM MOVIE")
    fun findAll(): List<Movie>

    @Query("DELETE FROM MOVIE")
    suspend fun clearAllPopularMovies()

    @Query("SELECT * FROM MOVIE")
    fun moviesPagingSource(): PagingSource<Int, Movie>
}
