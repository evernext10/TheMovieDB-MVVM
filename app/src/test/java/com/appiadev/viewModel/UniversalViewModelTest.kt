package com.appiadev.viewModel

import com.appiadev.MainCoroutineRule
import com.appiadev.TestData
import com.appiadev.fakes.FakeMovieRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

import org.junit.Test

@ExperimentalCoroutinesApi
class UniversalViewModelTest {

    private lateinit var viewModel: UniversalViewModel

    @get: Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun getAllMovies() {
        viewModel = UniversalViewModel(
            FakeMovieRepository()
        ).apply {
            this.getAllMovies()
        }

        val movieList = viewModel.movieList.value

        assertThat(movieList?.get(0)?.id).isEqualTo(1)
        assertThat(movieList?.get(0)?.title).isEqualTo(TestData.movie.title)
    }
}