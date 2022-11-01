package com.appiadev.model.core.states

import com.appiadev.model.api.MovieVideosResponse

sealed class StateMovieVideos {
    object Loading : StateMovieVideos()
    data class Success(val response: MovieVideosResponse) : StateMovieVideos()
    object Unauthorized : StateMovieVideos()
    object Error : StateMovieVideos()
}