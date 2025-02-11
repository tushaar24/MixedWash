package com.mixedwash

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform