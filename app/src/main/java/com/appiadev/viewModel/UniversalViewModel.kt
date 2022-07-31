package com.appiadev.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appiadev.model.api.MovieResponse
import com.appiadev.repository.UniversalRepository
import com.appiadev.utils.AppResult
import com.appiadev.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class UniversalViewModel(private val repository: UniversalRepository) : ViewModel() {

    val showLoading = ObservableBoolean()
    val countriesList = MutableLiveData<MovieResponse>()
    val showError = SingleLiveEvent<String?>()

    fun getAllMovies() {
        showLoading.set(true)
        viewModelScope.launch {
            val result = repository.getAllMovies()

            showLoading.set(false)
            when (result) {
                is AppResult.Success -> {
                    countriesList.value = result.successData!!
                    showError.value = null
                }
                is AppResult.Error -> showError.value = result.exception.message
            }
        }
    }
}
