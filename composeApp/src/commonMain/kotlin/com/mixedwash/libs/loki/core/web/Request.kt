package com.mixedwash.libs.loki.core.web

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CancellationException
import com.mixedwash.core.presentation.util.Logger
import com.mixedwash.libs.loki.core.web.exception.WebException

/**
 * Makes a web request to the specified URL and returns the result of the request.
 *
 * Internal API.
 *
 * @param Result The type of the result of the request.
 * @param url The URL to make the request to.
 * @param resultMapper A function that maps the HTTP response to the result type.
 * @return The result of the request.
 * @throws WebException If the request fails.
 */
public suspend fun <Result> HttpClient.makeRequest(
    url: String,
    resultMapper: suspend (HttpResponse) -> Result,
): Result {
    try {
        val response = get { url(url) }
        if (response.status.value in 200..299) {
            return resultMapper(response)
        } else {
            throw WebException(
                statusCode = response.status,
                message = "HTTP request failed with status code ${response.status.value}",
            )
        }
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (webException: WebException) {
        throw webException
    } catch (cause: Throwable) {
        Logger.e("ERROR", "Unable to make web request: ${cause.message}")
        throw cause
    }
}