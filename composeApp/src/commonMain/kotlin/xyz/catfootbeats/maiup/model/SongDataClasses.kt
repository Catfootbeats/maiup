package xyz.catfootbeats.maiup.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 曲目列表 API 响应
 */
@Serializable
data class SongListResponse(
    val songs: List<Song>,
    val genres: List<Genre> = emptyList(),
    val versions: List<MusicVersion> = emptyList()
)

/**
 * 曲目
 */
@Serializable
data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val genre: String,
    val bpm: Int,
    val version: Int,
    val difficulties: SongDifficulties
)

/**
 * 谱面难度集合
 */
@Serializable
data class SongDifficulties(
    val standard: List<SongDifficulty> = emptyList(),
    val dx: List<SongDifficulty> = emptyList(),
    val utage: List<SongDifficultyUtage> = emptyList()
)

/**
 * 谱面难度（标准/DX）
 */
@Serializable
data class SongDifficulty(
    val type: SongType,
    val difficulty: LevelIndex,
    val level: String,
    val level_value: Float,
    val note_designer: String,
    val version: Int
)

/**
 * 宴会场谱面难度
 */
@Serializable
data class SongDifficultyUtage(
    val kanji: String = "",
    val description: String = "",
    @SerialName("is_buddy")
    val isBuddy: Boolean = false
)

/**
 * 乐曲分类
 */
@Serializable
data class Genre(
    val id: Int,
    val title: String,
    val genre: String
)

/**
 * 曲目版本
 */
@Serializable
data class MusicVersion(
    val id: Int,
    val title: String,
    val version: Int
)
