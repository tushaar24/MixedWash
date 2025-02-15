package com.mixedwash.loki.autocomplete.googlemaps

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.encodeURLQueryComponent
import com.mixedwash.loki.autocomplete.AutocompletePlace
import com.mixedwash.loki.autocomplete.googlemaps.web.internal.AutocompleteApiResult
import com.mixedwash.loki.autocomplete.googlemaps.web.internal.resultOrThrow
import com.mixedwash.loki.autocomplete.toAutoCompletePlace
import com.mixedwash.loki.core.web.HttpApiEndpoint

class GoogleMapsAutocompleteEndpoint(
    private val apiKey: String
) : HttpApiEndpoint<String, List<AutocompletePlace>> {
    override fun url(param: String): String {
        val query = param.encodeURLQueryComponent()
        return "$BASE_URL?input=$query&key=$apiKey"
    }

    override suspend fun mapResponse(response: HttpResponse): List<AutocompletePlace> {
        val result = response.body<AutocompleteApiResult>()
            .resultOrThrow()

        return result.map { it.toAutoCompletePlace() }
    }

    companion object {

        /*
        * https://maps.googleapis.com/maps/api/place/autocomplete/json
              ?input=amoeba
              &location=37.76999%2C-122.44696
              &radius=500
              &types=establishment
              &key=YOUR_API_KEY
        * */

        private const val BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json"

    }

}