package com.appiadev.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appiadev.model.api.MovieResult
import com.appiadev.repository.UniversalRepository
import com.appiadev.utils.AppResult
import com.appiadev.utils.Constants
import com.appiadev.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class UniversalViewModel(private val repository: UniversalRepository) : ViewModel() {

    val showLoading = ObservableBoolean()
    val movieList = MutableLiveData<List<MovieResult>>()
    val showError = SingleLiveEvent<String?>()

    fun getAllMovies() {
        showLoading.set(true)
        viewModelScope.launch {
            val result = repository.getAllMovies()

            showLoading.set(false)
            when (result) {
                is AppResult.Success -> {
                    result.successData.movieResults!!.forEach {
                        it.posterPath = Constants().BASE_POSTER_PATH + it.posterPath
                    }
                    movieList.postValue(result.successData.movieResults!!)
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }
}
