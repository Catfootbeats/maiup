package xyz.catfootbeats.maiup.model

import kotlinx.serialization.Serializable

/**
 * 称号
 */
@Serializable
data class Trophy(
    val id: Int,
    val name: String,
    val genre: String,
    val color: String
)

/**
 * 头像
 */
@Serializable
data class Icon(
    val id: Int,
    val name: String,
    val genre: String
)

/**
 * 姓名框
 */
@Serializable
data class NamePlate(
    val id: Int,
    val name: String,
    val genre: String
)

/**
 * 背景
 */
@Serializable
data class Frame(
    val id: Int,
    val name: String,
    val genre: String
)

/**
 * API 响应包装类
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int,
    val data: T? = null
)
