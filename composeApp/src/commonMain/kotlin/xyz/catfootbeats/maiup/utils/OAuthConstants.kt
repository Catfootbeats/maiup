package xyz.catfootbeats.maiup.utils

object OAuthConstants {
    const val CLIENT_ID = "6d445a1e-2c91-4a57-a029-6f7bc522c732"
    const val REDIRECT_URI = "http://localhost:8086/callback"
    const val CALLBACK_PORT = 8086
    const val SCOPE = "read_user_profile+read_player+write_player"
    const val AUTHORIZE_URL = "https://maimai.lxns.net/oauth/authorize"

    fun buildAuthorizeUrl(codeChallenge: String): String =
        "$AUTHORIZE_URL?response_type=code&client_id=$CLIENT_ID" +
        "&redirect_uri=$REDIRECT_URI&scope=$SCOPE" +
        "&code_challenge_method=S256&code_challenge=$codeChallenge"
}
