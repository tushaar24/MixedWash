@file:Suppress("FunctionName")

package com.mixedwash.libs.loki.autocomplete

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import com.mixedwash.libs.loki.core.Place

/**
 * Represents an autocomplete service.
 *
 * @param T The type of the autocomplete results.
 */
public interface Autocomplete<T> {

    /**
     * The options for the autocomplete.
     */
    public val options: AutocompleteOptions

    /**
     * Performs an autocomplete search using the provided [query].
     *
     * @param query The query to search for.
     * @return The autocomplete result.
     */
    public suspend fun search(query: String): AutocompleteResult<T>

    public companion object
}

/**
 * Creates a new [Autocomplete] instance.
 *
 * @param T The type of the autocomplete results.
 * @param service The autocomplete service to use.
 * @param options The options for the autocomplete.
 * @param dispatcher The coroutine dispatcher to use.
 * @return A new [Autocomplete] instance.
 */
public fun <T> Autocomplete(
    service: AutocompleteService<T>,
    options: AutocompleteOptions = AutocompleteOptions(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Autocomplete<T> = com.mixedwash.libs.loki.autocomplete.internal.DefaultAutocomplete(
    service,
    options,
    dispatcher
)

/**
 * Creates a new [Autocomplete] instance for [Place]s.
 *
 * @param service The autocomplete service to use.
 * @param options The options for the autocomplete.
 * @param dispatcher The coroutine dispatcher to use.
 * @return A new [Autocomplete] instance.
 */
public fun PlaceAutocomplete(
    service: AutocompleteService<Place>,
    options: AutocompleteOptions = AutocompleteOptions(),
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
): Autocomplete<Place> = com.mixedwash.libs.loki.autocomplete.internal.DefaultAutocomplete(
    service,
    options,
    dispatcher
)