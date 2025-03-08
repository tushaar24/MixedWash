package com.mixedwash.features.laundryServices.data.repository

import com.mixedwash.features.laundryServices.data.entities.ServiceListResponseEntity
import com.mixedwash.features.laundryServices.data.service.remote.LaundryServicesService
import com.mixedwash.features.laundryServices.domain.repository.LaundryServiceRepository

class LaundryServicesRepositoryImpl  constructor(
    private val laundryServicesService: LaundryServicesService
) : LaundryServiceRepository {
    override suspend fun fetchAllLaundryServices(): ServiceListResponseEntity =
        laundryServicesService.fetchAllLaundryServices()
}