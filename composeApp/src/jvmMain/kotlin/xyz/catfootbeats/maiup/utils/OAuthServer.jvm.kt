package xyz.catfootbeats.maiup.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetAddress
import java.net.ServerSocket

actual suspend fun waitForOAuthCallback(port: Int): String {
    val serverSocket = ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))
    serverSocket.soTimeout = 120_000
    return try {
        val socket = serverSocket.accept()
        val reader = BufferedReader(InputStreamReader(socket.inputStream))
        val requestLine = reader.readLine()
        val code = requestLine
            .substringAfter("code=", "")
            .substringBefore(" ")
            .substringBefore("&")
        val writer = OutputStreamWriter(socket.outputStream)
        writer.write("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n\r\n")
        writer.write(successPage())
        writer.flush()
        socket.close()
        code
    } finally {
        serverSocket.close()
    }
}

private fun successPage(): String = """
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>授权成功</title>
<style>
  * { margin: 0; padding: 0; box-sizing: border-box; }
  body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
    background: #f5f5f7;
    display: flex; align-items: center; justify-content: center;
    min-height: 100vh; color: #1d1d1f;
  }
  .card {
    background: #fff; border-radius: 20px;
    padding: 48px 40px; text-align: center;
    box-shadow: 0 4px 24px rgba(0,0,0,0.08);
    max-width: 380px;
  }
  .icon {
    width: 64px; height: 64px;
    background: #34c759; border-radius: 50%;
    display: flex; align-items: center; justify-content: center;
    margin: 0 auto 24px;
  }
  .icon svg { fill: #fff; }
  h1 { font-size: 22px; font-weight: 600; margin-bottom: 8px; }
  p { font-size: 15px; color: #86868b; line-height: 1.5; }
</style>
</head>
<body>
<div class="card">
  <div class="icon">
    <svg width="32" height="32" viewBox="0 0 24 24"><path d="M9 16.17L4.83 12l-1.42 1.41L9 19 21 7l-1.41-1.41z"/></svg>
  </div>
  <h1>授权成功</h1>
  <p>你可以关闭此页面，返回应用继续使用。</p>
</div>
</body>
</html>
""".trimIndent()
