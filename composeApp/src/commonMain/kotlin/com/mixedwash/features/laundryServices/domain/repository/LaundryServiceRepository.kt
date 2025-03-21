package com.mixedwash.features.laundryServices.domain.repository

import com.mixedwash.features.laundryServices.data.entities.ServiceListResponseEntity

interface LaundryServiceRepository {
    suspend fun fetchAllLaundryServices(): ServiceListResponseEntity
}