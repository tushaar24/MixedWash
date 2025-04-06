package com.mixedwash.features.home.presentation

import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.navigation.Route
import com.mixedwash.features.address.domain.model.Address
import com.mixedwash.features.home.presentation.model.AddressBottomSheetState
import com.mixedwash.features.home.presentation.model.HomeBanner
import com.mixedwash.features.home.presentation.model.HomeOfferCard
import com.mixedwash.features.home.presentation.model.HomeScreenData
import com.mixedwash.features.home.presentation.model.HomeService
import com.mixedwash.features.home.presentation.model.IntroSection
import com.mixedwash.features.home.presentation.model.OrderStatus

data class HomeScreenState(
    val banner: HomeBanner? = null,
    val offerCards: List<HomeOfferCard>? = null,
    val services: List<HomeService>? = null,
    val activeOrders: List<OrderStatus>? = null,
    val introSection: IntroSection? = null,
    val isLoading: Boolean,
    val cartAddress: CartAddressState = CartAddressState.Unassigned,
    val addressBottomSheetState: AddressBottomSheetState? = null
)

fun HomeScreenData.toUiState(): HomeScreenState = HomeScreenState(
    banner = banners.firstOrNull { it.id == this.preferredBannerId } ?: banners[0],
    offerCards = offerCards,
    services = services,
    activeOrders = activeOrders,
    introSection = introSection,
    isLoading = false
)

sealed class CartAddressState {

    data class LocationError(
        val dialogPopupData: DialogPopupData,
        val issue: LocationIssue
    ) : CartAddressState()

    data class LocationFetched(
        val address: Address,
        val availability: ServiceAvailability = ServiceAvailability.Unassigned
    ) : CartAddressState()

    data object Unassigned : CartAddressState()

}

enum class LocationIssue {
    LocationDisabled,
    LocationPermissionDenied,
    LocationPermissionDeniedForever,
    LocationFetchError
}


sealed class ServiceAvailability {
    data class Unavailable(
        val title: String,
        val description: String,
        val currentLocationString: String,
        val imageUrl: String,
        val buttonText: String,
        val error: String? = null
    ) : ServiceAvailability()

    data object Available : ServiceAvailability()
    data object Unassigned : ServiceAvailability()
}


sealed class HomeScreenEvent {
    data class UpdateLocation(val address: Address? = null) : HomeScreenEvent()
    data object OnLocationSlabClicked : HomeScreenEvent()
    data object OnBannerClick : HomeScreenEvent()
    data object OnIntroClick : HomeScreenEvent()
    data class OnOfferClick(val offerId: String) : HomeScreenEvent()
    data object Reload : HomeScreenEvent()
    data object OnCloseAppRequest : HomeScreenEvent()
    data class OnServiceClicked(val serviceId: String) : HomeScreenEvent()
    data object OnSeeAllServicesClicked : HomeScreenEvent()
    data object OnNavigateToProfile : HomeScreenEvent()
    data object OnNavigateToFaqs : HomeScreenEvent()
    data object OnDismissedAddressBottomSheet : HomeScreenEvent()
    data object OnAvailabilityBottomSheetDismissed : HomeScreenEvent()
    sealed class AddressListEvent : HomeScreenEvent() {
        data class OnAddressClicked(val uid: String) : AddressListEvent()
        data object OnAddressSearchBoxClicked : AddressListEvent()
    }

    data object OnCloseAddressBottomSheet : HomeScreenEvent()
    data object OnScreenStart : HomeScreenEvent()
    data object OnChangeLocation : HomeScreenEvent()
}

sealed class HomeScreenUiEvent {
    data class ShowSnackbar(val payload: SnackbarPayload) : HomeScreenUiEvent()
    data class Navigate(val route: Route) : HomeScreenUiEvent()
    data object CloseApp : HomeScreenUiEvent()
    data object DismissAddressBottomSheet : HomeScreenUiEvent()
    data object DismissAvailabilityBottomSheet : HomeScreenUiEvent()
}

