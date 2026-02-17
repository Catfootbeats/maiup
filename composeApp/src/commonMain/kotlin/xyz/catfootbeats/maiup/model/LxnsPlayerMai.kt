package xyz.catfootbeats.maiup.model

import kotlinx.serialization.Serializable

/**
 * 玩家数据类
 * @property name 游戏内名称
 * @property rating 玩家 DX Rating
 * @property friend_code 好友码
 * @property course_rank 段位 ID
 * @property class_rank 阶级 ID
 * @property star 搭档觉醒数
 * @property trophy 称号
 * @property icon 头像
 * @property name_plate 姓名框
 * @property frame 背景
 * @property upload_time 玩家被同步时的 UTC 时间（仅获取玩家信息返回）
 */
@Serializable
data class LxnsPlayerMai(
    val name: String = "maimai",
    val rating: Int = 0,
    val friend_code: Long = 0,
    val course_rank: Int = 0,
    val class_rank: Int = 0,
    val star: Int = 0,
    val trophy: Trophy = Trophy(0,"","",""),
    val icon: Icon = Icon(0,"",""),
    val name_plate: NamePlate = NamePlate(0,"",""),
    val frame: Frame = Frame(0,"",""),
    val upload_time: String = "未知"
)
