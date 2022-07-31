package com.appiadev.repository

import android.content.Context
import android.util.Log
import com.appiadev.api.ServerAPI
import com.appiadev.utils.AppResult
import com.appiadev.utils.NetworkManager.isOnline
import com.appiadev.utils.handleApiError
import com.appiadev.utils.handleSuccess
import com.appiadev.utils.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UniversalRepositoryImpl(
    private val api: ServerAPI,
    private val context: Context
) : UniversalRepository {

    override suspend fun getAllCountries(): AppResult<List<String>> {
        if (isOnline(context)) {
            return try {
                val response = api.getAllCountries()
                if (response.isSuccessful) {
                    // save the data
                    response.body()?.let {
                        withContext(Dispatchers.IO) {
                            // dao.add(it)
                        }
                    }
                    handleSuccess(response)
                } else {
                    handleApiError(response)
                }
            } catch (e: Exception) {
                AppResult.Error(e)
            }
        } else {
            // check in db if the data exists
            val data = getCountriesDataFromCache()
            return if (data.isNotEmpty()) {
                Log.d("DB", "from db")
                AppResult.Success(data)
            } else
            // no network
                context.noNetworkConnectivityError()
        }
    }

    private suspend fun getCountriesDataFromCache(): List<String> {
        return withContext(Dispatchers.IO) {
            // dao.findAll()
            emptyList()
        }
    }
}
