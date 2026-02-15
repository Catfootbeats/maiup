package xyz.catfootbeats.maiup

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.core.context.startKoin
import org.koin.dsl.module


class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        // 判断是否为平板
        requestedOrientation = if (resources.configuration.screenWidthDp < 600) {
            // 如果是手机，锁定为竖屏
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            // 如果是平板，允许自由旋转
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        startKoin {
            val contextModule = module {
                // 将 Application Context 作为单例注入
                single<android.content.Context> { this@MainActivity }
            }

            modules(
                contextModule,
                //appModule() // 这是你的共享模块，定义在 commonMain
            )
        }
        setContent {
            App()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        //设置沉浸式虚拟键，在MIUI系统中，虚拟键背景透明。原生系统中，虚拟键背景半透明。
    }
}
