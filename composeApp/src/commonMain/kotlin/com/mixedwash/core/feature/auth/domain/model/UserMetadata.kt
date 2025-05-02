package com.mixedwash.core.feature.auth.domain.model

import com.mixedwash.features.address.domain.model.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserMetadata(
    @SerialName("uid") val uid: String,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("last_open_time_stamp") val lastOpenTimeStamp: Long?,
    @SerialName("email") val email: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("user_serviceable") val userServiceable: Boolean? = null,
    @SerialName("address_list") val addressList: List<Address>,
    @SerialName("current_address_id") val defaultAddressId: String? = null,
    @SerialName("user_type") val userType: UserType = UserType.CUSTOMER
)

@Serializable
enum class UserType {
    @SerialName("customer")
    CUSTOMER
}
