package com.appiadev.viewModel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.appiadev.MainCoroutineRule
import com.appiadev.fakes.FakeMovieRepository
import com.appiadev.ui.home.navigation.list.viewModel.MovieListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class MovieListViewModelTest {

    private lateinit var viewModel: MovieListViewModel

    val lifecycleOwner: LifecycleOwner = mock(LifecycleOwner::class.java)
    val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))

    @get: Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun getAllMovies() {
        viewModel = MovieListViewModel(
            FakeMovieRepository()
        ).apply {
            this.postRecommendedMoviePage(page = 1)
            this.postTrendsMoviePage(page = 1)
            this.postUpcomingMoviePage(page = 1)
        }

        lifecycle.markState(Lifecycle.State.RESUMED)
        Mockito.`when`(lifecycleOwner.lifecycle).thenReturn(lifecycle)
    }
}
