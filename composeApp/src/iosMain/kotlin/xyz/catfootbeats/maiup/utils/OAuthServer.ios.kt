package xyz.catfootbeats.maiup.utils

actual suspend fun waitForOAuthCallback(port: Int): String {
    throw NotImplementedError("OAuth callback not implemented for iOS")
}
