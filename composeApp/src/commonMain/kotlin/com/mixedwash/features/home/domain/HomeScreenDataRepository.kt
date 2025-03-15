package com.mixedwash.features.home.domain

import com.mixedwash.core.domain.models.Result
import com.mixedwash.features.home.data.models.HomeScreenDataDto

interface HomeScreenDataRepository {
    suspend fun fetchData() :Result<HomeScreenDataDto>
}