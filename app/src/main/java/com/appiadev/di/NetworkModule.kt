package com.appiadev.di

import android.content.Context
import com.appiadev.R
import com.appiadev.utils.Constants
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    val connectTimeout: Long = 40 // 20s
    val readTimeout: Long = 40 // 20s

    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url
            val url = originalUrl.newBuilder()
                .addQueryParameter("api_key", Constants().API_KEY)
                .build()

            val requestBuilder = originalRequest.newBuilder().url(url)
            val newRequest = requestBuilder.build()
            chain.proceed(newRequest)
        }
    }

    fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                return@Interceptor chain.proceed(chain.request())
            } catch (e: Exception) {
                val cacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(1, TimeUnit.DAYS)
                    .build()
                val offlineRequest: Request = chain.request().newBuilder()
                    .cacheControl(cacheControl)
                    .removeHeader("Pragma")
                    .build()
                return@Interceptor chain.proceed(offlineRequest)
            }
        }
    }

    fun provideHttpClient(context: Context): OkHttpClient {
        val httpCacheDirectory = File(context.cacheDir, "offlineCache")
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.cache(Cache(httpCacheDirectory, 10 * 1024 * 1024))
        okHttpClientBuilder.addInterceptor(provideApiKeyInterceptor())
        okHttpClientBuilder.addInterceptor(provideOfflineCacheInterceptor())
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient(get()) }
    single {
        val baseUrl = androidContext().getString(R.string.BASE_URL)
        provideRetrofit(get(), baseUrl)
    }
}
