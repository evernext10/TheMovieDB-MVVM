package com.appiadev.list

import com.appiadev.MainCoroutineRule
import com.appiadev.TestData
import com.appiadev.model.api.Movie
import com.appiadev.model.api.MovieResponse
import com.appiadev.runBlockingTest
import com.appiadev.utils.Constants
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoadMovieListUseCaseTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun loadMovieList_successResult() = coroutineRule.runBlockingTest {
        // GIVEN
        val result = TestData.listResult

        // WHEN
        result.forEach {
            it.posterPath = Constants().BASE_POSTER_PATH + it.posterPath
        }

        // THEN
        assertThat(result.first()).isInstanceOf(Movie::class.java)
        assertThat((result as MovieResponse).movieResults?.get(0)!!.id).isEqualTo(1)
    }
}
