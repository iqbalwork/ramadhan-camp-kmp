package com.iqbalwork.ramadhancamp.shared.common.utils

import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class IosShareManager : ShareManager {
    override fun shareText(text: String) {
        val window = UIApplication.sharedApplication.keyWindow ?: return
        val rootViewController = window.rootViewController ?: return

        val activityViewController = UIActivityViewController(
            activityItems = listOf(text),
            applicationActivities = null
        )

        rootViewController.presentViewController(
            activityViewController,
            animated = true,
            completion = null
        )
    }
}
