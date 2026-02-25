package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.InputField
import top.yukonga.miuix.kmp.basic.SearchBar
import top.yukonga.miuix.kmp.basic.Text

@Composable
fun SongSearchPage(){
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(top = 16.dp).fillMaxSize()) {
        SearchBar(
            inputField = {
                InputField(
                    query = searchText,
                    onQueryChange = { searchText = it },
                    onSearch = { /* 处理搜索操作 */ },
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            // 搜索结果内容
            Column {
                // 在这里添加搜索建议或结果
            }
        }
    }
}