package xyz.catfootbeats.maiup

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform