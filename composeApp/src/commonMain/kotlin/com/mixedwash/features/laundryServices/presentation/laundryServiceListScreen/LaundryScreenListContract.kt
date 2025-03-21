package com.mixedwash.features.laundryServices.presentation.laundryServiceListScreen

import androidx.compose.runtime.Stable
import com.mixedwash.features.laundryServices.data.entities.ServiceResponseEntity

@Stable
data class LaundryScreenListUIState(
    val laundryServiceList : List<ServiceResponseEntity>
)

sealed class LaundryScreenListUiEvents{
    data class ShowLaundryServices(
        val laundryServiceList: List<ServiceResponseEntity>
    ): LaundryScreenListUiEvents()
}