package com.appiadev.db.daos

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

    @Query("SELECT * FROM MOVIE WHERE type = :type_")
    fun findAllMoviesByType(type_: String): List<Movie>
}
