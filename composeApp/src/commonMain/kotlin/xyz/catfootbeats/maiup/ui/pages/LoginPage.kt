package xyz.catfootbeats.maiup.ui.pages

import androidx.compose.foundation.layout.Arrangement
 import androidx.compose.foundation.layout.Box
 import androidx.compose.foundation.layout.Column
 import androidx.compose.foundation.layout.Spacer
 import androidx.compose.foundation.layout.height
 import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.viewmodel.MaiupViewModel

@Composable
fun LoginPage() {
    val maiupViewModel: MaiupViewModel = koinViewModel()
    val isAuthorizing by maiupViewModel.isAuthorizing.collectAsState()
    val oauthError by maiupViewModel.oauthError.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.width(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MaiUp!!!",
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(40.dp))

            Button(
                onClick = { maiupViewModel.authorizeOAuth() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("落雪 OAuth 授权")
            }

            if (isAuthorizing) {
                Spacer(Modifier.height(24.dp))
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "等待浏览器授权...",
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            oauthError?.let { error ->
                Spacer(Modifier.height(16.dp))
                Text(
                    text = error,
                    style = MiuixTheme.textStyles.body2,
                    color = MiuixTheme.colorScheme.error
                )
            }
        }
    }
}
