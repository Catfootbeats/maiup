package xyz.catfootbeats.maiup.utils

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openUrl(url: String) {
    val nsUrl = NSURL(string = url)
    nsUrl.let {
        UIApplication.sharedApplication.openURL(it)
    }
}
