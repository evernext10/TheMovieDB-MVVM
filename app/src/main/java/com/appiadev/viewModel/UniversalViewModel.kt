package com.appiadev.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appiadev.model.core.MovieFilterType
import com.appiadev.model.core.MovieType
import com.appiadev.model.core.State
import com.appiadev.repository.UniversalRepository
import com.appiadev.utils.AppResult
import com.appiadev.utils.Constants
import com.appiadev.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class UniversalViewModel(private val repository: UniversalRepository) : ViewModel() {

    val showLoading = ObservableBoolean()

    private val _upcomingMovieList = MutableLiveData<State>()
    val upcomingMovieList: LiveData<State> = _upcomingMovieList

    private val _trendsMovieList = MutableLiveData<State>()
    val trendsMovieList: LiveData<State> = _trendsMovieList

    private val _recommendedMovieList = MutableLiveData<State>()
    val recommendedMovieList: LiveData<State> = _recommendedMovieList

    val showError = SingleLiveEvent<String?>()

    private var upcomingMoviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private var trendsMoviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private var recommendedMoviePageLiveData: MutableLiveData<Int> = MutableLiveData()

    init {
        this.upcomingMoviePageLiveData.observeForever {
            getMoviesByTypeAndPage(MovieType.Upcoming, _upcomingMovieList, it)
        }

        this.trendsMoviePageLiveData.observeForever {
            getMoviesByTypeAndPage(MovieType.Trends, _trendsMovieList, it)
        }

        this.recommendedMoviePageLiveData.observeForever {
            getMoviesByTypeAndPage(MovieType.Recommended, _recommendedMovieList, it)
        }
    }

    fun postUpcomingMoviePage(page: Int) = upcomingMoviePageLiveData.postValue(page)
    fun postTrendsMoviePage(page: Int) = trendsMoviePageLiveData.postValue(page)
    fun postRecommendedMoviePage(page: Int) = recommendedMoviePageLiveData.postValue(page)

    fun getRecommendedMoviesByFilter(
        filter: MovieFilterType
    ) {
        getMoviesByTypeAndPage(
            MovieType.Recommended,
            _recommendedMovieList,
            recommendedMoviePageLiveData.value ?: 1,
            filter
        )
    }
    private fun getMoviesByTypeAndPage(
        type: MovieType,
        _state: MutableLiveData<State>,
        page: Int,
        filterType: MovieFilterType? = null
    ) {
        showLoading.set(true)
        viewModelScope.launch {
            val result = when (type) {
                MovieType.Upcoming -> repository.getUpcomingMovies(page)
                MovieType.Trends -> repository.getTrendsMovies(page)
                else -> when (filterType) {
                    is MovieFilterType.Language -> {
                        repository.getRecommendedMovies("language", page)
                    }
                    else -> {
                        repository.getRecommendedMovies("year", page)
                    }
                }
            }
            showLoading.set(false)
            when (result) {
                is AppResult.Success -> {
                    result.successData.movieResults!!.forEach {
                        it.posterPath = Constants().BASE_POSTER_PATH + it.posterPath
                    }
                    _state.postValue(State.Success(result.successData.movieResults!!))
                    showError.value = null
                }
                is AppResult.Error -> {
                    _state.postValue(State.Error)
                    showError.value = result.exception.message
                }
            }
        }
    }
}
