package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.TabRowWithContour

@Composable
fun RankPage(){
    val tabs = listOf("B50", "历史成绩")
    val pagerState = rememberPagerState { tabs.size }
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.currentPage
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    Surface(modifier = Modifier.padding(16.dp)) {
        Column {
            TabRowWithContour(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when(page){
                    0 -> B50Page()
                    1 -> HistoryPage()
                }
            }
        }
    }
}