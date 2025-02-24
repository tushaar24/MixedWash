package com.mixedwash.presentation.components.dump

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

const val BASE_URL = "https://assets-aac.pages.dev/assets/"

@Composable
fun AsyncImageLoader(
    imageUrl: String,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        modifier = modifier,
        model = "$BASE_URL$imageUrl.png",
        contentDescription = contentDescription,
        contentScale = contentScale,
        onError = { println(it.result.throwable.printStackTrace()) }
    )
}