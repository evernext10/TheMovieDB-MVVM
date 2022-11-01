package com.appiadev.ui.home.navigation.detail.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appiadev.model.core.MovieFilterType
import com.appiadev.model.core.MovieType
import com.appiadev.model.core.states.StateMovieDetail
import com.appiadev.model.core.states.StateMovieList
import com.appiadev.model.core.states.StateMovieVideos
import com.appiadev.ui.home.navigation.detail.repository.MovieDetailRepository
import com.appiadev.ui.home.navigation.list.repository.MovieListRepository
import com.appiadev.utils.AppResult
import com.appiadev.utils.Constants
import com.appiadev.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val repository: MovieDetailRepository) : ViewModel() {

    val showLoading = ObservableBoolean()

    private val _movieDetail = MutableLiveData<StateMovieDetail>()
    val movieDetail: LiveData<StateMovieDetail> = _movieDetail

    private val _movieVideos = MutableLiveData<StateMovieVideos>()
    val movieVideos: LiveData<StateMovieVideos> = _movieVideos

    val showError = SingleLiveEvent<String?>()

    fun getMovieDetailById(
        id: Int
    ) {
        showLoading.set(true)
        viewModelScope.launch {
            val result = repository.getMovieDetail(id)
            showLoading.set(false)
            when (result) {
                is AppResult.Success -> {
                    _movieDetail.postValue(StateMovieDetail.Success(result.successData))
                    showError.value = null
                }
                is AppResult.Error -> {
                    _movieDetail.postValue(StateMovieDetail.Error)
                    showError.value = result.exception.message
                }
            }
        }
    }
}
