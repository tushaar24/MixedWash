package com.mixedwash.domain.util

fun String.capitalize(): String {
    val capitalizedWords = this.lowercase(). split(" ")
        .map { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
    return capitalizedWords.joinToString(" ")
}