package com.appiadev.model.core

import com.appiadev.model.api.Movie

sealed class State {
    object Loading : State()
    data class Success(val movies: List<Movie>) : State()
    object Unauthorized : State()
    object Error : State()
}