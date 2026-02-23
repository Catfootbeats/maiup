package xyz.catfootbeats.maiup.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.catfootbeats.maiup.model.ApiResponse
import xyz.catfootbeats.maiup.model.Game
import xyz.catfootbeats.maiup.model.LxnsPlayerMai
import xyz.catfootbeats.maiup.model.RatingTrend
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.model.getApiName

/**
 * 玩家数据 API
 * @param client HttpClient 实例
 * @param baseUrl API 基础 URL
 */
class LxnsApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://maimai.lxns.net/api/v0"
) {

    /**
     * 获取玩家信息
     * @param userToken 用户 Token，用于 API 认证
     * @return API 响应，包含玩家信息
     */
    suspend fun getPlayerInfo(userToken: String, game: Game = Game.MAI): ApiResponse<LxnsPlayerMai> {
        return client.get("$baseUrl/user/${game.getApiName()}/player") {
            header("X-User-Token", userToken)
            contentType(ContentType.Application.Json)
        }.body()
    }

    /**
     * 获取玩家 Rating 趋势
     * @param userToken 用户 Token，用于 API 认证
     * @param version 游戏版本号，默认为 25000
     * @return API 响应，包含 Rating 趋势数据列表
     */
    suspend fun getPlayerTrend(
        userToken: String,
        game: Game = Game.MAI,
        version: Int = 25000
    ): ApiResponse<List<RatingTrend>> {
        return client.get("$baseUrl/user/${game.getApiName()}/player/trend") {
            header("X-User-Token", userToken)
            parameter("version", version)
            contentType(ContentType.Application.Json)
        }.body()
    }

    /**
     * 上传玩家成绩
     * @param userToken 用户 Token，用于 API 认证
     * @param scores 玩家成绩列表
     * @return API 响应，包含上传结果
     */
    suspend fun uploadPlayerScores(userToken: String, scores: List<Score>, game: Game = Game.MAI): ApiResponse<Unit> {
        return client.post("$baseUrl/user/${game.getApiName()}/player/scores") {
            header("X-User-Token", userToken)
            contentType(ContentType.Application.Json)
            setBody(mapOf("scores" to scores))
        }.body()
    }
}
