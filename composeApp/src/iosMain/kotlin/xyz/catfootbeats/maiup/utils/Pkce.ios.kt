package xyz.catfootbeats.maiup.utils

actual fun generateCodeVerifier(): String {
    throw NotImplementedError("PKCE not implemented for iOS")
}

actual fun computeCodeChallenge(verifier: String): String {
    throw NotImplementedError("PKCE not implemented for iOS")
}
