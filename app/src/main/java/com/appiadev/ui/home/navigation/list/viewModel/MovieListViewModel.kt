package com.appiadev.ui.home.navigation.list.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appiadev.model.core.MovieFilterType
import com.appiadev.model.core.MovieType
import com.appiadev.model.core.states.StateMovieList
import com.appiadev.ui.home.navigation.list.repository.MovieListRepository
import com.appiadev.utils.AppResult
import com.appiadev.utils.Constants
import com.appiadev.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class MovieListViewModel(private val repository: MovieListRepository) : ViewModel() {

    val showLoading = ObservableBoolean()

    private val _upcomingMovieList = MutableLiveData<StateMovieList>()
    val upcomingMovieList: LiveData<StateMovieList> = _upcomingMovieList

    private val _trendsMovieList = MutableLiveData<StateMovieList>()
    val trendsMovieList: LiveData<StateMovieList> = _trendsMovieList

    private val _recommendedMovieList = MutableLiveData<StateMovieList>()
    val recommendedMovieList: LiveData<StateMovieList> = _recommendedMovieList

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
        _state: MutableLiveData<StateMovieList>,
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
                    _state.postValue(StateMovieList.Success(result.successData.movieResults!!))
                    showError.value = null
                }
                is AppResult.Error -> {
                    _state.postValue(StateMovieList.Error)
                    showError.value = result.exception.message
                }
            }
        }
    }
}
