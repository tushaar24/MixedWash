package com.mixedwash.features.laundryServices.domain.useCases.impl

import com.mixedwash.features.laundryServices.data.entities.ServiceListResponseEntity
import com.mixedwash.features.laundryServices.domain.repository.LaundryServiceRepository
import com.mixedwash.features.laundryServices.domain.useCases.FetchAllLaundryServicesUseCase

class FetchAllLaundryServicesUseCaseImpl(
    private val laundryServiceRepository: LaundryServiceRepository
): FetchAllLaundryServicesUseCase {
    override suspend fun fetchAllLaundryServices() : ServiceListResponseEntity {
        return laundryServiceRepository.fetchAllLaundryServices()
    }
}