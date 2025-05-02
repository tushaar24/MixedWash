package com.mixedwash.libs.loki.core

/**
 * Represents a place on Earth.
 *
 * The geocoder might not always return all of these properties, so you must check each one before
 * using it.
 *
 * @property coordinates The geographic coordinates of the place.
 * @property name The name of the place.
 * @property street The street of the place.
 * @property isoCountryCode The ISO country code of the place.
 * @property country The country of the place.
 * @property postalCode The postal code of the place.
 * @property administrativeArea The administrative area of the place.
 * @property subAdministrativeArea The sub-administrative area of the place.
 * @property locality The locality of the place.
 * @property subLocality The sub-locality of the place.
 * @property thoroughfare The thoroughfare of the place.
 * @property subThoroughfare The sub-thoroughfare of the place.
 *
 */
class Place(
    val coordinates: Coordinates,
    val name: String?,
    val street: String?,
    val route: String?,
    val neighbourhood: String?,
    val political: String?,
    val isoCountryCode: String?,
    val country: String?,
    val postalCode: String?,
    val formattedAddress: String?,
    val administrativeArea: String?,
    val subAdministrativeArea: String?,
    val locality: String?,
    val subLocality: String?,
    val thoroughfare: String?,
    val subThoroughfare: String?,
    val googlePlaceId: String? = null,
) {

    /**
     * Check if all of the properties are null.
     *
     * @return True if all properties are null, false otherwise.
     */
    val isEmpty: Boolean = name == null && street == null && isoCountryCode == null &&
            country == null && postalCode == null && administrativeArea == null &&
            subAdministrativeArea == null && locality == null && subLocality == null &&
            thoroughfare == null && subThoroughfare == null

    /**
     * Get the first non-null value of the place.
     *
     * Ensure [isEmpty] returns `true` before calling this method, or catch the exception.
     *
     * @return The first non-null value of the place.
     * @throws IllegalStateException If the place is empty.
     */
    val firstValue: String
        get() = name ?: street ?: thoroughfare ?: subThoroughfare ?: subLocality ?: locality
        ?: subAdministrativeArea ?: administrativeArea ?: postalCode ?: country ?: isoCountryCode
        ?: throw IllegalStateException("Place is empty")

    /**
     * Used for testing purposes.
     */
    internal fun nullCopy(
        coordinates: Coordinates = this.coordinates,
        name: String? = null,
        street: String? = null,
        isoCountryCode: String? = null,
        country: String? = null,
        postalCode: String? = null,
        administrativeArea: String? = null,
        subAdministrativeArea: String? = null,
        locality: String? = null,
        subLocality: String? = null,
        thoroughfare: String? = null,
        subThoroughfare: String? = null,
    ): Place = Place(
        coordinates = coordinates,
        name = name,
        street = street,
        isoCountryCode = isoCountryCode,
        country = country,
        postalCode = postalCode,
        administrativeArea = administrativeArea,
        subAdministrativeArea = subAdministrativeArea,
        locality = locality,
        subLocality = subLocality,
        thoroughfare = thoroughfare,
        subThoroughfare = subThoroughfare,
        googlePlaceId = googlePlaceId,
        route = route,
        neighbourhood = neighbourhood,
        political = political,
        formattedAddress = formattedAddress,
    )

    override fun toString(): String {
        return "Place(" +
                "coordinates=$coordinates, " +
                "name=$name, " +
                "street=$street, " +
                "isoCountryCode=$isoCountryCode, " +
                "country=$country, " +
                "postalCode=$postalCode, " +
                "administrativeArea=$administrativeArea, " +
                "subAdministrativeArea=$subAdministrativeArea, " +
                "locality=$locality, " +
                "subLocality=$subLocality, " +
                "thoroughfare=$thoroughfare, " +
                "subThoroughfare=$subThoroughfare, " +
                "googlePlaceId=$googlePlaceId" +
                "route=$route" +
                "neighbourhood=$neighbourhood" +
                "political=$political" +
                "formattedAddress=$formattedAddress" +
                ")"
    }
}