package xyz.catfootbeats.maiup.utils

/** 启动本地 HTTP 服务等待 OAuth 回调，返回授权码 */
expect suspend fun waitForOAuthCallback(port: Int): String
