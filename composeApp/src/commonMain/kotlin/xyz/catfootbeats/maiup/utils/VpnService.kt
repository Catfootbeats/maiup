package xyz.catfootbeats.maiup.utils

expect class VpnService {
    fun start()
    fun stop()
    val isRunning: Boolean
}