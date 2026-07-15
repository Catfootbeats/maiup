package xyz.catfootbeats.maiup.utils

interface SyncProxyServer {
    val isRunning: Boolean
    fun start(port: Int, onCaptured: (String) -> Unit)
    fun stop()
}

expect fun createSyncProxyServer(): SyncProxyServer
