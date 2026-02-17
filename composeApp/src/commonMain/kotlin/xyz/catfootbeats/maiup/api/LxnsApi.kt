package xyz.catfootbeats.maiup.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import xyz.catfootbeats.maiup.model.ApiResponse
import xyz.catfootbeats.maiup.model.LxnsPlayerMai

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
    suspend fun getPlayerInfoMai(userToken: String): ApiResponse<LxnsPlayerMai> {
        return client.get("$baseUrl/user/maimai/player") {
            header("X-User-Token", userToken)
            contentType(ContentType.Application.Json)
        }.body()
    }

    /**
     * 上传玩家信息
     * @param lxnsPlayerMai 玩家信息
     * @return 上传结果
     */
    suspend fun uploadPlayerInfoMai(lxnsPlayerMai: LxnsPlayerMai): Boolean {
        return try {
            // TODO: 实现上传
            client.post("$baseUrl/maimai/player") {
                contentType(ContentType.Application.Json)
                setBody(lxnsPlayerMai)
            }.status == HttpStatusCode.OK
        } catch (_: Exception) {
            false
        }
    }
}
