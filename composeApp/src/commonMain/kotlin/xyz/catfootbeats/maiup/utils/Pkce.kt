package xyz.catfootbeats.maiup.utils

/** 生成 PKCE code_verifier（随机字符串） */
expect fun generateCodeVerifier(): String

/** 对 verifier 做 SHA-256 哈希后 base64url 编码，得到 code_challenge */
expect fun computeCodeChallenge(verifier: String): String
