package com.mixedwash.loki.autocomplete


/**
 * Options for configuring an [Autocomplete].
 *
 * @property minimumQuery The minimum number of characters required in a query before an
 * autocomplete request is made.
 */
public open class AutocompleteOptions(
    public val minimumQuery: Int = DEFAULT_MINIMUM_QUERY,
) {

    internal companion object {

        private const val DEFAULT_MINIMUM_QUERY = 3
    }
}