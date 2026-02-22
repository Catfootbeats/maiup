package xyz.catfootbeats.maiup.model

import kotlinx.serialization.SerialName
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
 * Rating 趋势数据
 * @property total 总 Rating
 * @property standard_total 标准 Rating
 * @property dx_total DX Rating
 * @property date 日期
 */
@Serializable
data class RatingTrend(
    val total: Int,
    val standard_total: Int,
    val dx_total: Int,
    val date: String
)

/**
 * 难度
 * 宴谱为0
 */
@Serializable
enum class LevelIndex{
    BASIC,ADVANCED,EXPERT,MASTER,ReMASTER
}
/**
 * API 响应包装类
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int,
    val data: T? = null
)

/**
 * FULL COMBO 类型
 */
@Serializable
enum class FCType {
    @SerialName("fc") FC,
    @SerialName("fcp") FCP,
    @SerialName("ap") AP,
    @SerialName("app") APP
}

/**
 * FULL SYNC 类型
 */
@Serializable
enum class FSType {
    @SerialName("sync") SYNC,
    @SerialName("fs") FS,
    @SerialName("fsp") FSP,
    @SerialName("fsd") FSD,
    @SerialName("fsdp") FSDP
}

/**
 * 评级类型
 */
@Serializable
enum class RateType {
    @SerialName("d") D,
    @SerialName("c") C,
    @SerialName("b") B,
    @SerialName("bb") BB,
    @SerialName("bbb") BBB,
    @SerialName("a") A,
    @SerialName("aa") AA,
    @SerialName("aaa") AAA,
    @SerialName("s") S,
    @SerialName("sp") S_PLUS,
    @SerialName("ss") SS,
    @SerialName("ssp") SS_PLUS,
    @SerialName("sss") SSS,
    @SerialName("sssp") SSS_PLUS
}

/**
 * 谱面类型
 */
@Serializable
enum class SongType {
    @SerialName("standard") STANDARD,
    @SerialName("dx") DX,
    @SerialName("utage") UTAGE
}

/**
 * 玩家成绩数据类
 * @property id 曲目 ID
 * @property song_name 曲名（仅获取 Score 时返回）
 * @property level 难度标级，如 14+
 * @property level_index 难度索引
 * @property achievements 达成率
 * @property fc FULL COMBO 类型（可空）
 * @property fs FULL SYNC 类型（可空）
 * @property dx_score DX 分数
 * @property dx_star DX 星级，最大值为 5
 * @property dx_rating DX Rating（仅获取 Score 时返回）
 * @property rate 评级类型（仅获取 Score 时返回）
 * @property type 谱面类型
 * @property play_time 游玩的 UTC 时间，精确到分钟（可空）
 * @property upload_time 成绩被同步时的 UTC 时间（仅获取 Score 时返回）
 * @property last_played_time 谱面最后游玩的 UTC 时间（仅获取成绩列表、获取最佳成绩时返回）
 */
@Serializable
data class Score(
    val id: Int,
    val song_name: String = "",
    val level: String,
    val level_index: LevelIndex,
    val achievements: Float,
    val fc: FCType? = null,
    val fs: FSType? = null,
    val dx_score: Int,
    val dx_star: Int,
    val dx_rating: Float = 0f,
    val rate: RateType = RateType.D,
    val type: SongType,
    val play_time: String? = null,
    val upload_time: String = "",
    val last_played_time: String = ""
)
