package xyz.catfootbeats.maiup.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.catfootbeats.maiup.model.ApiResponse
import xyz.catfootbeats.maiup.model.Best50
import xyz.catfootbeats.maiup.model.Game
import xyz.catfootbeats.maiup.model.LxnsPlayerMai
import xyz.catfootbeats.maiup.model.OAuthTokenResponse
import xyz.catfootbeats.maiup.model.RatingTrend
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.model.SongListResponse
import xyz.catfootbeats.maiup.model.getApiName

const val apiUrl: String = "https://maimai.lxns.net/api/v0"

class LxnsApi(
    private val client: HttpClient,
    private val baseUrl: String = apiUrl
) {

    // ---- OAuth 令牌（PKCE） ----

    suspend fun exchangeToken(
        clientId: String,
        code: String,
        redirectUri: String,
        codeVerifier: String
    ): ApiResponse<OAuthTokenResponse> {
        return client.post("$baseUrl/oauth/token") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "client_id" to clientId,
                "grant_type" to "authorization_code",
                "code" to code,
                "redirect_uri" to redirectUri,
                "code_verifier" to codeVerifier
            ))
        }.body()
    }

    suspend fun refreshToken(
        clientId: String,
        refreshToken: String
    ): ApiResponse<OAuthTokenResponse> {
        return client.post("$baseUrl/oauth/token") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "client_id" to clientId,
                "grant_type" to "refresh_token",
                "refresh_token" to refreshToken
            ))
        }.body()
    }

    // ---- 玩家数据 API（Bearer 认证） ----

    suspend fun getPlayerInfo(bearerToken: String, game: Game = Game.MAI): ApiResponse<LxnsPlayerMai> {
        return client.get("$baseUrl/user/${game.getApiName()}/player") {
            header("Authorization", "Bearer $bearerToken")
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getPlayerTrend(
        bearerToken: String,
        game: Game = Game.MAI,
        version: Int = 25000
    ): ApiResponse<List<RatingTrend>> {
        return client.get("$baseUrl/user/${game.getApiName()}/player/trend") {
            header("Authorization", "Bearer $bearerToken")
            parameter("version", version)
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getPlayerB50(
        bearerToken: String,
        game: Game = Game.MAI,
    ): ApiResponse<Best50> {
        return client.get("$baseUrl/user/${game.getApiName()}/player/bests") {
            header("Authorization", "Bearer $bearerToken")
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun uploadPlayerScores(
        bearerToken: String,
        scores: List<Score>,
        game: Game = Game.MAI
    ): ApiResponse<Unit> {
        return client.post("$baseUrl/user/${game.getApiName()}/player/scores") {
            header("Authorization", "Bearer $bearerToken")
            contentType(ContentType.Application.Json)
            setBody(mapOf("scores" to scores))
        }.body()
    }

    // ---- 历史成绩 ----

    suspend fun getPlayerScores(
        bearerToken: String,
        game: Game = Game.MAI
    ): ApiResponse<List<Score>> {
        return client.get("$baseUrl/user/${game.getApiName()}/player/scores") {
            header("Authorization", "Bearer $bearerToken")
            contentType(ContentType.Application.Json)
        }.body()
    }

    // ---- 曲目数据（公共接口） ----

    suspend fun getSongList(version: Int = 25500): ApiResponse<SongListResponse> {
        return client.get("$baseUrl/maimai/song/list") {
            parameter("version", version)
            contentType(ContentType.Application.Json)
        }.body()
    }
}
