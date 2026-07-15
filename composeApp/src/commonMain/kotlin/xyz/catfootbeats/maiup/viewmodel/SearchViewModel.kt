package xyz.catfootbeats.maiup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.catfootbeats.maiup.api.LxnsApi
import xyz.catfootbeats.maiup.model.FCType
import xyz.catfootbeats.maiup.model.FSType
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.MusicVersion
import xyz.catfootbeats.maiup.model.Score
import xyz.catfootbeats.maiup.model.Song
import xyz.catfootbeats.maiup.model.SongType
import xyz.catfootbeats.maiup.utils.ApiCallHandler

enum class SortField(val label: String) {
    ACHIEVEMENTS("达成率"),
    DX_RATING("DX Rating"),
    LEVEL_VALUE("定数"),
    DX_SCORE("DX 分数")
}

class SearchViewModel(
    private val lxnsApi: LxnsApi,
) : ViewModel() {
    private val apiHandler = ApiCallHandler(viewModelScope)

    // ---- 数据状态 ----

    private val _scores = MutableStateFlow<List<Score>>(emptyList())
    val scores: StateFlow<List<Score>> = _scores.asStateFlow()

    private val _songMap = MutableStateFlow<Map<Int, Song>>(emptyMap())
    val songMap: StateFlow<Map<Int, Song>> = _songMap.asStateFlow()

    private val _versionList = MutableStateFlow<List<MusicVersion>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // ---- 筛选状态 ----

    val searchQuery = MutableStateFlow("")

    val selectedLevelIndex = MutableStateFlow<Set<LevelIndex>>(emptySet())
    val selectedSongType = MutableStateFlow<Set<SongType>>(emptySet())
    val selectedVersion = MutableStateFlow<Set<Int>>(emptySet())

    val minLevelValue = MutableStateFlow(1.0f)
    val maxLevelValue = MutableStateFlow(15.0f)

    val selectedDxStar = MutableStateFlow<Set<Int>>(emptySet())

    val fcFilter = MutableStateFlow<FCType?>(null)
    val fsFilter = MutableStateFlow<FSType?>(null)

    val sortBy = MutableStateFlow(SortField.DX_RATING)
    val sortDescending = MutableStateFlow(true)

    // 筛选区域展开状态
    private val _filterExpanded = MutableStateFlow(false)
    val filterExpanded: StateFlow<Boolean> = _filterExpanded.asStateFlow()

    // 过滤后的成绩
    private val _filteredScores = MutableStateFlow<List<Score>>(emptyList())
    val filteredScores: StateFlow<List<Score>> = _filteredScores.asStateFlow()

    private val _songCount = MutableStateFlow(0)
    val songCount: StateFlow<Int> = _songCount.asStateFlow()

    // ---- 数据加载 ----

    fun loadData(token: String) {
        if (_isLoading.value || token.isEmpty()) return
        apiHandler.cancelAll()
        _isLoading.value = true
        _error.value = null

        // 加载 B50 成绩（已验证可用）
        apiHandler.call(
            apiCall = { lxnsApi.getPlayerScores(token) },
            onSuccess = {
                val combined = mutableListOf<Score>()
                if (it != null) {
                    combined.addAll(it)
                }
                _scores.value = combined
                _isLoading.value = false
                recalculateFiltered()
                loadSongList()
            },
            onError = {
                _isLoading.value = false
                _error.value = it
            }
        )
    }

    private fun loadSongList() {
        apiHandler.call(
            apiCall = { lxnsApi.getSongList() },
            onSuccess = {
                if (it != null) {
                    _songMap.value = it.songs.associateBy { song -> song.id }
                    _versionList.value = it.versions
                }
                recalculateFiltered()
            },
            onError = {
                // 歌曲列表加载失败不影响主要功能，静默处理
                recalculateFiltered()
            }
        )
    }

    // ---- 筛选逻辑 ----

    private fun recalculateFiltered() {
        val scores = _scores.value
        val songs = _songMap.value
        val query = searchQuery.value
        val levels = selectedLevelIndex.value
        val types = selectedSongType.value
        val versions = selectedVersion.value
        val minLv = minLevelValue.value
        val maxLv = maxLevelValue.value
        val stars = selectedDxStar.value
        val fc = fcFilter.value
        val fs = fsFilter.value
        val sort = sortBy.value
        val desc = sortDescending.value

        var result = scores

        // 1. 搜索过滤
        if (query.isNotBlank()) {
            result = result.filter { it.song_name.contains(query, ignoreCase = true) }
        }

        // 2. 难度筛选（空集合 = 全选）
        if (levels.isNotEmpty()) {
            result = result.filter { it.level_index in levels }
        }

        // 3. 谱面类型筛选
        if (types.isNotEmpty()) {
            result = result.filter { it.type in types }
        }

        // 4. 版本筛选
        if (versions.isNotEmpty()) {
            result = result.filter { score ->
                val songVersion = songs[score.id]?.version
                songVersion != null && songVersion in versions
            }
        }

        // 5. 谱面定数筛选
        result = result.filter { score ->
            val lv = getLevelValue(score, songs)
            lv == null || (lv >= minLv && lv <= maxLv)
        }

        // 6. DX 星级筛选
        if (stars.isNotEmpty()) {
            result = result.filter { it.dx_star in stars }
        }

        // 7. FC 筛选
        if (fc != null) {
            result = result.filter { it.fc == fc }
        }

        // 8. FS 筛选
        if (fs != null) {
            result = result.filter { it.fs == fs }
        }

        // 9. 排序
        result = when (sort) {
            SortField.ACHIEVEMENTS -> result.sortedBy { it.achievements }
            SortField.DX_RATING -> result.sortedBy { it.dx_rating }
            SortField.LEVEL_VALUE -> result.sortedBy { getLevelValue(it, songs) ?: 0f }
            SortField.DX_SCORE -> result.sortedBy { it.dx_score }
        }
        if (desc) result = result.reversed()

        _filteredScores.value = result
        _songCount.value = result.size
    }

    // ---- 筛选器操作方法 ----

    fun toggleLevelIndex(level: LevelIndex) {
        selectedLevelIndex.value = selectedLevelIndex.value.toggle(level)
        recalculateFiltered()
    }

    fun toggleSongType(type: SongType) {
        selectedSongType.value = selectedSongType.value.toggle(type)
        recalculateFiltered()
    }

    fun toggleVersion(version: Int) {
        selectedVersion.value = selectedVersion.value.toggle(version)
        recalculateFiltered()
    }

    fun toggleDxStar(star: Int) {
        selectedDxStar.value = selectedDxStar.value.toggle(star)
        recalculateFiltered()
    }

    fun setFcFilter(fc: FCType?) {
        fcFilter.value = if (fcFilter.value == fc) null else fc
        recalculateFiltered()
    }

    fun setFsFilter(fs: FSType?) {
        fsFilter.value = if (fsFilter.value == fs) null else fs
        recalculateFiltered()
    }

    fun setSortField(field: SortField) {
        if (sortBy.value == field) {
            sortDescending.value = !sortDescending.value
        } else {
            sortBy.value = field
            sortDescending.value = true
        }
        recalculateFiltered()
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        recalculateFiltered()
    }

    fun updateMinLevelValue(value: Float) {
        minLevelValue.value = value
        recalculateFiltered()
    }

    fun updateMaxLevelValue(value: Float) {
        maxLevelValue.value = value
        recalculateFiltered()
    }

    fun toggleFilterExpanded() {
        _filterExpanded.value = !_filterExpanded.value
    }

    fun clearAllFilters() {
        searchQuery.value = ""
        selectedLevelIndex.value = emptySet()
        selectedSongType.value = emptySet()
        selectedVersion.value = emptySet()
        minLevelValue.value = 1.0f
        maxLevelValue.value = 15.0f
        selectedDxStar.value = emptySet()
        fcFilter.value = null
        fsFilter.value = null
        sortBy.value = SortField.DX_RATING
        sortDescending.value = true
        recalculateFiltered()
    }

    fun getVersionMap(): Map<Int, String> {
        return _versionList.value.associate { it.version to it.title }
    }

    // ---- 清理 ----

    override fun onCleared() {
        super.onCleared()
        apiHandler.cancelAll()
    }

    companion object {
        /**
         * 获取谱面定数，通过 songMap 交叉查询
         */
        fun getLevelValue(score: Score, songMap: Map<Int, Song>): Float? {
            val song = songMap[score.id] ?: return null
            val diffs = when (score.type) {
                SongType.STANDARD -> song.difficulties.standard
                SongType.DX -> song.difficulties.dx
                SongType.UTAGE -> null // 宴会场无定数
            }
            return diffs?.find { it.difficulty == score.level_index }?.level_value
        }
    }
}

/**
 * 切换集合中的元素：存在则移除，不存在则添加
 */
private fun <T> Set<T>.toggle(element: T): Set<T> {
    return if (element in this) this - element else this + element
}
