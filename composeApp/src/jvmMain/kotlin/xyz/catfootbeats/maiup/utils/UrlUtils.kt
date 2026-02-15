package xyz.catfootbeats.maiup.utils

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    try {
        val desktop = Desktop.getDesktop()
        if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(url))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
