package xyz.catfootbeats.maiup.utils

actual fun createSyncProxyServer(): SyncProxyServer = object : SyncProxyServer {
    override val isRunning: Boolean get() = false
    override fun start(port: Int, onCaptured: (String) -> Unit) {}
    override fun stop() {}
}
