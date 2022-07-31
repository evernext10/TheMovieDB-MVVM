package com.appiadev

import com.appiadev.model.api.Movie
import com.appiadev.model.api.MovieResponse

object TestData {
    val movie = Movie(
        id = 1,
        title = "Title1",
        posterPath = "path1",
        voteAverage = 3.5,
        voteCount = 5
    )

    val movieApiModel1 = MovieResponse(
        page = 1,
        movieResults = listOf(movie),
        totalPages = 1
    )

    val listResult = listOf(
        movie,
        movie,
        movie
    )
}
