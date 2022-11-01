package com.appiadev.remote

import com.appiadev.MainCoroutineRule
import com.appiadev.TestData
import com.appiadev.api.ServerAPI
import com.appiadev.model.api.Movie
import com.appiadev.runBlockingTest
import com.appiadev.ui.home.navigation.list.repository.MovieListRepositoryImpl
import com.appiadev.utils.Constants
import com.appiadev.utils.handleApiError
import com.appiadev.utils.handleSuccess
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class LoadMovieListUseCaseTest {

    private lateinit var repository: MovieListRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: ServerAPI

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(ServerAPI::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun loadMovieList_successResult() = coroutineRule.runBlockingTest {
        // GIVEN
        val result = TestData.movieApiModel1

        // WHEN
        result.movieResults?.forEach {
            it.posterPath = Constants().BASE_POSTER_PATH + it.posterPath
        }

        // THEN
        assertThat(result.movieResults?.first()).isInstanceOf(Movie::class.java)
        assertThat(result.movieResults?.get(0)!!.id).isEqualTo(1)
    }

    @Test
    fun `Get recommendations movies, valid response, returns success`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(TestData.listResult.toString())
        )

        val result = api.getRecommended(1)
        assertThat(handleSuccess(result)).isNotNull()
    }

    @Test
    fun `Get recommendations movies, malformed response, returns failure`() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setBody(TestData.listResult.toString())
        )

        val result = api.getRecommended(1)
        assertThat(handleApiError(result)).isNull()
    }
}
