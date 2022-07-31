package com.appiadev.repository

import com.appiadev.utils.AppResult

interface UniversalRepository {
    suspend fun getAllCountries(): AppResult<List<String>>
}
