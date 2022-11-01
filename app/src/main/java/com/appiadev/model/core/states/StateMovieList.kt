package com.appiadev.model.core.states

import com.appiadev.model.api.Movie

sealed class StateMovieList {
    object Loading : StateMovieList()
    data class Success(val movies: List<Movie>) : StateMovieList()
    object Unauthorized : StateMovieList()
    object Error : StateMovieList()
}