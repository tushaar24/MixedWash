package com.mixedwash.libs.loki.autocomplete.internal

import com.mixedwash.libs.loki.autocomplete.Autocomplete
import com.mixedwash.libs.loki.autocomplete.AutocompleteOptions
import com.mixedwash.libs.loki.autocomplete.AutocompleteResult
import com.mixedwash.libs.loki.autocomplete.AutocompleteService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class DefaultAutocomplete<T>(
    private val service: AutocompleteService<T>,
    override val options: AutocompleteOptions,
    private val dispatcher: CoroutineDispatcher,
) : Autocomplete<T> {

    override suspend fun search(query: String): AutocompleteResult<T> {
        if (!service.isAvailable()) {
            return AutocompleteResult.NotSupported
        }

        try {
            if (query.length <= options.minimumQuery) {
                return AutocompleteResult.Success(emptyList())
            }

            val result = withContext(dispatcher) {
                service.search(query)
            }

            return AutocompleteResult.Success(result)
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (cause: Throwable) {
            return AutocompleteResult.Failed(cause.message ?: "An unknown error occurred")
        }
    }
}