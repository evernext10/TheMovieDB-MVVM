package com.appiadev.repository

import com.appiadev.MainCoroutineRule
import com.appiadev.TestData
import com.appiadev.api.ServerAPI
import com.appiadev.runBlockingTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DefaultMovieRepositoryTest {

    @get: Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    lateinit var movieService: ServerAPI

    @Before
    fun setup() = coroutineRule.runBlockingTest {
        Mockito.`when`(movieService.getAllMovies(page)).thenReturn(Response.success(TestData.movieApiModel1))
    }

    @Test
    fun getMovieList_returnsCorrectList() = coroutineRule.runBlockingTest {
        val list = TestData.listResult

        assertThat(list.first().id).isEqualTo(1)
        assertThat(list.first().title).isEqualTo(TestData.movie.title)
        assertThat(list.first().voteAverage).isInstanceOf(Double::class.java)
    }
}
