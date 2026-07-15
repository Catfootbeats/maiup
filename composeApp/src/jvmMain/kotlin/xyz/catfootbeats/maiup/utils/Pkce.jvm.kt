package xyz.catfootbeats.maiup.utils

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

actual fun generateCodeVerifier(): String {
    val random = SecureRandom()
    val bytes = ByteArray(32)
    random.nextBytes(bytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
}

actual fun computeCodeChallenge(verifier: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(verifier.toByteArray())
    return Base64.getUrlEncoder().withoutPadding().encodeToString(hash)
}
