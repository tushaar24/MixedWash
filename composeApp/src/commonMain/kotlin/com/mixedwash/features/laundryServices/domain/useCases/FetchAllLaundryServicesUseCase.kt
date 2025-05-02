package com.mixedwash.features.laundryServices.domain.useCases

import com.mixedwash.features.laundryServices.data.entities.ServiceListResponseEntity

interface FetchAllLaundryServicesUseCase{
    suspend fun fetchAllLaundryServices(): ServiceListResponseEntity
}