package com.stanmation.mydiary

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform