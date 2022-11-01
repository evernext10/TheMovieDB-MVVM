package com.appiadev.model.core.states

import com.appiadev.model.api.Movie

sealed class StateMovieDetail {
    object Loading : StateMovieDetail()
    data class Success(val movie: Movie) : StateMovieDetail()
    object Unauthorized : StateMovieDetail()
    object Error : StateMovieDetail()
}