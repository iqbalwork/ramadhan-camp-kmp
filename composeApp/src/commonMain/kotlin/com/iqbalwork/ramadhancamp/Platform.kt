package com.iqbalwork.ramadhancamp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform