package xyz.catfootbeats.maiup.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import xyz.catfootbeats.maiup.utils.openUrl

@Composable
fun OthersCard() {
    val iconItems = remember {
        listOf(
            IconItemData(
                icon = IconSource.Url("https://map.bemanicn.com/favicon.png"),
                text = "音游地图",
                webUrl = "https://map.bemanicn.com/"
            ),
            IconItemData(
                icon = IconSource.Url("https://maimai.lxns.net/favicon.webp"),
                text = "落雪查分器",
                webUrl = "https://maimai.lxns.net/"
            ),
            IconItemData(
                icon = IconSource.Url("https://www.diving-fish.com/favicon.ico"),
                text = "水鱼查分器",
                webUrl = "https://www.diving-fish.com/maimaidx/prober/"
            ),
            IconItemData(
                icon = IconSource.Url("https://shama.dxrating.net/images/version-logo/circle.webp"),
                text = "DXRating",
                webUrl = "https://dxrating.net/"
            ),
            IconItemData(
                icon = IconSource.Url("https://union.godserver.cn/assets/png/icon-BDHGr2IZ.png"),
                text = "Union",
                webUrl = "https://union.godserver.cn/"
            ),
        )
    }
    Column {
        SmallTitle("其他")
        Card(Modifier.fillMaxWidth()){
                FlowRow(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    iconItems.forEach { item ->
                        IconGridItem(
                            icon = item.icon,
                            text = item.text,
                            webUrl = item.webUrl,
                            openExternal = item.openExternal
                        )
                    }
                }
        }
    }

}

sealed class IconSource {
    data class Vector(val imageVector: ImageVector) : IconSource()
    data class Url(val url: String) : IconSource()
    data class Resource(val resourcePath: DrawableResource) : IconSource()
}

data class IconItemData(
    val icon: IconSource,
    val text: String,
    val webUrl: String,
    val openExternal: Boolean = true
)

@Composable
fun IconGridItem(
    icon: IconSource,
    text: String,
    webUrl: String,
    openExternal: Boolean = true
) {
    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(16 .dp))
            .clickable{
                if(openExternal){
                    openUrl(webUrl)
                }
            }
            .size(80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (icon) {
                is IconSource.Vector -> {
                    Icon(
                        imageVector = icon.imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                    )
                }
                is IconSource.Url -> {
                    AsyncImage(
                        model = icon.url,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
                is IconSource.Resource -> {
                    Image(
                        painter = painterResource(icon.resourcePath),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MiuixTheme.textStyles.body2,
                maxLines = 1
            )
        }
    }
}
