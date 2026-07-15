package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardColors
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.model.FCType
import xyz.catfootbeats.maiup.model.FSType
import xyz.catfootbeats.maiup.model.LevelIndex
import xyz.catfootbeats.maiup.model.SongType
import xyz.catfootbeats.maiup.ui.components.ScoreCard
import xyz.catfootbeats.maiup.viewmodel.SearchViewModel
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel
import xyz.catfootbeats.maiup.viewmodel.SortField

@Composable
fun SearchPage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val searchViewModel: SearchViewModel = koinViewModel()
    val settings by maiupViewModel.settings.collectAsState()

    LaunchedEffect(settings.isAuthorized) {
        if (settings.isAuthorized) {
            val token = maiupViewModel.tryRefreshToken()
            searchViewModel.loadData(token)
        }
    }

    val filteredScores by searchViewModel.filteredScores.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()
    val error by searchViewModel.error.collectAsState()
    val searchQuery by searchViewModel.searchQuery.collectAsState()
    val selectedLevelIndex by searchViewModel.selectedLevelIndex.collectAsState()
    val selectedSongType by searchViewModel.selectedSongType.collectAsState()
    val selectedVersion by searchViewModel.selectedVersion.collectAsState()
    val minLevelValue by searchViewModel.minLevelValue.collectAsState()
    val maxLevelValue by searchViewModel.maxLevelValue.collectAsState()
    val selectedDxStar by searchViewModel.selectedDxStar.collectAsState()
    val fcFilter by searchViewModel.fcFilter.collectAsState()
    val fsFilter by searchViewModel.fsFilter.collectAsState()
    val sortBy by searchViewModel.sortBy.collectAsState()
    val sortDescending by searchViewModel.sortDescending.collectAsState()
    val filterExpanded by searchViewModel.filterExpanded.collectAsState()
    val songCount by searchViewModel.songCount.collectAsState()
    val versionList = remember(searchViewModel) { searchViewModel.getVersionMap() }
    var searchExpanded by remember { mutableStateOf(false) }



    if (error != null) {
        Box(Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(error!!, color = MiuixTheme.colorScheme.error)
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // 搜索栏
        InputField(
            query = searchQuery,
            onQueryChange = { searchViewModel.updateSearchQuery(it) },
            onSearch = { },
            expanded = searchExpanded,
            onExpandedChange = { searchExpanded = it }
        )

        // 排序选择器
        SortRow(
            currentSort = sortBy,
            descending = sortDescending,
            onSortSelected = { searchViewModel.setSortField(it) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp)
        )

        // 筛选区域切换
        Row(
            modifier = Modifier.fillMaxWidth()
                .clickable { searchViewModel.toggleFilterExpanded() }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("筛选 (${filteredScores.size})", fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "清除",
                    color = MiuixTheme.colorScheme.primary,
                    modifier = Modifier.clickable { searchViewModel.clearAllFilters() }
                )
                Spacer(Modifier.width(8.dp))
                Text(if (filterExpanded) "▲" else "▼")
            }
        }

        // 可折叠筛选区域
        if (filterExpanded) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipGroup(
                    label = "难度",
                    items = LevelIndex.entries,
                    selected = selectedLevelIndex,
                    itemLabel = { it.name },
                    onToggle = { searchViewModel.toggleLevelIndex(it) }
                )
                FilterChipGroup(
                    label = "分类",
                    items = SongType.entries,
                    selected = selectedSongType,
                    itemLabel = { it.name },
                    onToggle = { searchViewModel.toggleSongType(it) }
                )
                if (versionList.isNotEmpty()) {
                    FilterLabel("版本")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        versionList.forEach { (versionId, versionName) ->
                            val isSelected = versionId in selectedVersion
                            ChipItem(
                                label = versionName,
                                selected = isSelected,
                                onClick = { searchViewModel.toggleVersion(versionId) }
                            )
                        }
                    }
                }
                FilterChipGroup(
                    label = "DX 星",
                    items = (1..5).toList(),
                    selected = selectedDxStar,
                    itemLabel = { "★".repeat(it) },
                    onToggle = { searchViewModel.toggleDxStar(it) }
                )
                Column {
                    FilterLabel("全连击")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ChipItem("全部", fcFilter == null) { searchViewModel.setFcFilter(null) }
                        FCType.entries.forEach { fc ->
                            ChipItem(fc.name, fcFilter == fc) { searchViewModel.setFcFilter(fc) }
                        }
                    }
                }
                Column {
                    FilterLabel("全同步")
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ChipItem("全部", fsFilter == null) { searchViewModel.setFsFilter(null) }
                        FSType.entries.forEach { fs ->
                            ChipItem(fs.name, fsFilter == fs) { searchViewModel.setFsFilter(fs) }
                        }
                    }
                }
                Text(
                    "谱面定数: ${minLevelValue.to1Decimal()} ~ ${maxLevelValue.to1Decimal()}",
                    style = MiuixTheme.textStyles.footnote1
                )
                LevelValueSlider(
                    title = "最小定数",
                    value = minLevelValue,
                    onValueChange = { searchViewModel.updateMinLevelValue(it) }
                )
                LevelValueSlider(
                    title = "最大定数",
                    value = maxLevelValue,
                    onValueChange = { searchViewModel.updateMaxLevelValue(it) }
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        } else if (filteredScores.isEmpty()) {
            Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text("无成绩", color = MiuixTheme.colorScheme.onSurface)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(filteredScores) { score ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .defaultMinSize(230.dp)
                                    .padding(4.dp),
                            ) {
                                ScoreCard(
                                    score = score,
                                    levelValue = SearchViewModel.getLevelValue(score, searchViewModel.songMap.value)
                                )
                            }
                }
            }
            Text(
                "共 $songCount 条成绩",
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                color = MiuixTheme.colorScheme.onSurface,
                style = MiuixTheme.textStyles.footnote2
            )
        }
    }
}

// ---- 子组件 ----

@Composable
private fun SortRow(
    currentSort: SortField,
    descending: Boolean,
    onSortSelected: (SortField) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        SortField.entries.forEach { field ->
            val isSelected = currentSort == field
            Card(
                modifier = Modifier.clickable { onSortSelected(field) },
                colors = CardColors(
                    if (isSelected) MiuixTheme.colorScheme.primaryContainer else MiuixTheme.colorScheme.surface,
                    if (isSelected) MiuixTheme.colorScheme.onPrimaryContainer else MiuixTheme.colorScheme.onSurface
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(field.label, style = MiuixTheme.textStyles.footnote1)
                    if (isSelected) {
                        Spacer(Modifier.width(4.dp))
                        Text(if (descending) "▼" else "▲", style = MiuixTheme.textStyles.footnote2)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterLabel(label: String) {
    Text(label, style = MiuixTheme.textStyles.footnote1)
    Spacer(Modifier.height(4.dp))
}

@Composable
private fun <T> FilterChipGroup(
    label: String,
    items: List<T>,
    selected: Set<T>,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    onToggle: (T) -> Unit,
) {
    Column(modifier = modifier) {
        FilterLabel(label)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items.forEach { item ->
                ChipItem(itemLabel(item), item in selected) { onToggle(item) }
            }
        }
    }
}

@Composable
private fun ChipItem(label: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.clickable(onClick = onClick),
        colors = CardColors(
            if (selected) MiuixTheme.colorScheme.primaryContainer else MiuixTheme.colorScheme.surface,
            if (selected) MiuixTheme.colorScheme.onPrimaryContainer else MiuixTheme.colorScheme.onSurface
        )
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MiuixTheme.textStyles.footnote2)
    }
}

@Composable
private fun LevelValueSlider(title: String, value: Float, onValueChange: (Float) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, style = MiuixTheme.textStyles.footnote2)
        Text("-", modifier = Modifier.clickable { onValueChange((value - 0.1f).coerceAtLeast(1.0f)) }.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
        Text(value.to1Decimal(), style = MiuixTheme.textStyles.footnote2)
        Text("+", modifier = Modifier.clickable { onValueChange((value + 0.1f).coerceAtMost(15.0f)) }.padding(horizontal = 8.dp, vertical = 4.dp), fontWeight = FontWeight.Bold)
    }
}

private fun Float.to1Decimal(): String {
    val rounded = (this * 10).toInt()
    return "${rounded / 10}.${kotlin.math.abs(rounded % 10)}"
}
