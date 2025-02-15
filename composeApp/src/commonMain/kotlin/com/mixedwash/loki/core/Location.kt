package com.mixedwash.loki.core

/**
 * Object representing a user's location.
 *
 * @property coordinates The coordinates of the location.
 * @property accuracy The accuracy of the location in meters, these values can be empty depending on
 * the accuracy of the location request.
 * @property azimuth The azimuth of the location in degrees, this value can be empty depending on
 * the accuracy of the location request.
 * @property speed The speed of the location in meters per second, this value can be empty depending
 * on the accuracy of the location request.
 * @property altitude The altitude of the location in meters, this value can be empty depending on
 * the accuracy of the location request.
 * @property timestampMillis The timestamp of the location in milliseconds since epoch.
 */
 class Location(
     val coordinates: Coordinates,
     val accuracy: Double,
     val azimuth: Azimuth?,
     val speed: Speed?,
     val altitude: Altitude?,
     val timestampMillis: Long,
)

/**
 * Object representing the users azimuth.
 *
 * **Note:** These values can be empty depending on the accuracy of the location request.
 *
 * @property degrees The azimuth in degrees.
 * @property accuracy The accuracy of the azimuth in degrees.
 */
 class Azimuth(
     val degrees: Float,
     val accuracy: Float?,
)

/**
 * Object representing the speed of the user.
 *
 * **Note:** These values can be empty depending on the accuracy of the location request.
 *
 * @property mps The speed in meters per second.
 * @property accuracy The accuracy of the speed in meters per second.
 */
 class Speed(
     val mps: Float,
     val accuracy: Float?,
)

/**
 * Object representing the altitude of the user.
 *
 * **Note:** These values can be empty depending on the accuracy of the location request.
 *
 * @property meters The altitude in meters.
 * @property accuracy The accuracy of the altitude in meters.
 */
 class Altitude(
     val meters: Double,
     val accuracy: Float?,
)