package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
 import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.theme.MiuixTheme
 import xyz.catfootbeats.maiup.utils.SyncProxyServer
 import xyz.catfootbeats.maiup.utils.createSyncProxyServer
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

@Composable
fun SyncPage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val settings by maiupViewModel.settings.collectAsState()
     val proxy: SyncProxyServer = remember { createSyncProxyServer() }
    var isRunning by remember { mutableStateOf(false) }
    val logs = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 代理控制
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text(
                    text = "本地代理同步",
                    style = MiuixTheme.textStyles.title2,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = if (isRunning) "代理状态：运行中（端口 8080）" else "代理状态：未启动",
                    style = MiuixTheme.textStyles.body2,
                    color = if (isRunning) MiuixTheme.colorScheme.onPrimaryContainer
                            else MiuixTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            if (isRunning) {
                                proxy.stop()
                                isRunning = false
                            } else {
                                proxy.start(8080) { log ->
                                    logs.add(0, log)
                                }
                                isRunning = true
                            }
                        },
                         modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isRunning) "停止代理" else "启动代理")
                    }
                }
            }
        }

        // 配置说明
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                SmallTitle("手机代理设置")

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "1. 确保手机和电脑在同一网络",
                    style = MiuixTheme.textStyles.body2
                )
                Text(
                    text = "2. 手机 Wi-Fi 设置中配置 HTTP 代理",
                    style = MiuixTheme.textStyles.body2
                )
                Text(
                    text = "3. 地址填电脑 IP，端口 8080",
                    style = MiuixTheme.textStyles.body2
                )
                Text(
                    text = "4. 打开微信舞萌 DX 小程序查看成绩",
                    style = MiuixTheme.textStyles.body2
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "或用 Clash 配置代理：",
                    style = MiuixTheme.textStyles.body2,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = """
proxies:
  - name: maiup
    type: http
    server: proxy.maimai.lxns.net
    port: 8080
rules:
  - DOMAIN-SUFFIX,wahlap.com,maiup
  - MATCH,DIRECT
                        """.trimIndent(),
                        style = MiuixTheme.textStyles.footnote2,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(Modifier.height(12.dp))

                TextButton(
                    text = "复制 Clash 配置",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { /* TODO: clipboard */ }
                )
            }
        }

        // 捕获日志
        if (logs.isNotEmpty()) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    SmallTitle("捕获记录（${logs.size}）")
                    Spacer(Modifier.height(8.dp))
                    logs.take(20).forEach { log ->
                        Text(
                            text = log,
                            style = MiuixTheme.textStyles.footnote2,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
