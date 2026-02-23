package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.SelectionContainer
import top.yukonga.miuix.kmp.basic.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun ErrorCard(
    msg: String,
    title: String = "Error"
) {
    Card(
        modifier = Modifier.padding(bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
                Text(
                    text = title,
                    style = MiuixTheme.textStyles.title2,
                    color = MiuixTheme.colorScheme.error
                )
            SelectionContainer {
                Text(
                    text = msg,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.error
                )
            }
        }
    }
}
