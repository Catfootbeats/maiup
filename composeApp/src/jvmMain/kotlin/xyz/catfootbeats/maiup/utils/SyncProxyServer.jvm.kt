package xyz.catfootbeats.maiup.utils

import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

actual fun createSyncProxyServer(): SyncProxyServer = JvmSyncProxyServer()

private class JvmSyncProxyServer : SyncProxyServer {
    @Volatile
    private var serverSocket: ServerSocket? = null
    @Volatile
    private var active = false

    override val isRunning: Boolean get() = active

    override fun start(port: Int, onCaptured: (String) -> Unit) {
        if (active) return
        active = true
        Thread.ofPlatform().name("proxy-accept").start {
            try {
                serverSocket = ServerSocket(port, 50, InetAddress.getByName("0.0.0.0"))
                while (active) {
                    val client = serverSocket!!.accept()
                    Thread.ofPlatform().start { handleClient(client, onCaptured) }
                }
            } catch (_: Exception) { }
            finally { active = false; serverSocket?.close(); serverSocket = null }
        }
    }

    override fun stop() { active = false; serverSocket?.close(); serverSocket = null }

    private fun handleClient(client: Socket, onCaptured: (String) -> Unit) {
        try {
            client.use { c ->
                val reader = c.getInputStream().bufferedReader()
                val requestLine = reader.readLine() ?: return
                val parts = requestLine.split(" ")
                when (parts.getOrNull(0)) {
                    "CONNECT" -> handleConnect(c, parts.getOrNull(1) ?: "")
                    else -> handleHttp(c, reader, requestLine, onCaptured)
                }
            }
        } catch (_: Exception) { }
    }

    private fun handleConnect(client: Socket, target: String) {
        val hostPort = target.split(":")
        val host = hostPort.getOrNull(0) ?: return
        val port = hostPort.getOrNull(1)?.toIntOrNull() ?: 443
        try {
            val remote = Socket(host, port)
            client.getOutputStream().write("HTTP/1.1 200 Connection Established\r\n\r\n".toByteArray())
            tunnel(client.getInputStream(), remote.getOutputStream(),
                remote.getInputStream(), client.getOutputStream())
            remote.close()
        } catch (_: Exception) { }
    }

    private fun handleHttp(client: Socket, reader: java.io.BufferedReader, requestLine: String, onCaptured: (String) -> Unit) {
        val parts = requestLine.split(" ", limit = 3)
        if (parts.size < 3) return
        val uri = java.net.URI(parts[1])
        val host = uri.host ?: return
        val port = if (uri.port > 0) uri.port else 80
        val headers = mutableListOf<String>()
        var line = reader.readLine()
        while (!line.isNullOrEmpty()) { headers.add(line); line = reader.readLine() }
        val hasBody = headers.any { it.startsWith("Content-Length:", true) }
         val contentLength = headers.firstOrNull { it.startsWith("Content-Length:", true) }?.substringAfter(":")?.trim()?.toIntOrNull() ?: 0
         val body = if (hasBody && contentLength > 0) {
             val chars = CharArray(contentLength)
             reader.read(chars, 0, contentLength)
             String(chars)
         } else ""
        try {
            val remote = Socket(host, port)
            val req = buildString {
                append("${parts[0]} ${uri.path}${if (uri.query != null) "?${uri.query}" else ""} HTTP/1.1\r\n")
                append("Host: $host\r\n")
                headers.forEach { if (!it.startsWith("Host:", true)) append("$it\r\n") }
                append("Connection: close\r\n\r\n"); if (hasBody) append(body)
            }
            remote.getOutputStream().write(req.toByteArray())
            val resp = String(remote.getInputStream().readAllBytes())
            client.getOutputStream().write(resp.toByteArray())
            if (host.contains("wahlap") || host.contains("maimai"))
                onCaptured("[HTTP] $requestLine — ${resp.lines().firstOrNull()}")
            remote.close()
        } catch (_: Exception) { }
    }

    private fun tunnel(in1: InputStream, out1: OutputStream, in2: InputStream, out2: OutputStream) {
        val t1 = Thread.ofPlatform().start { try { in1.copyTo(out1) } catch (_: Exception) {} }
        val t2 = Thread.ofPlatform().start { try { in2.copyTo(out2) } catch (_: Exception) {} }
        t1.join(); t2.join()
    }
}
