package xyz.catfootbeats.maiup.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import org.koin.core.context.GlobalContext

actual fun openUrl(url: String) {
    try {
        val context = GlobalContext.get().get<Context>()
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
