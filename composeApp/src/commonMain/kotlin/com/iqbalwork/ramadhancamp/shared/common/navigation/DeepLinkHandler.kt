package com.iqbalwork.ramadhancamp.shared.common.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object DeepLinkHandler {
    private val _deepLinks = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val deepLinks: SharedFlow<String> = _deepLinks

    fun handleDeepLink(uri: String) {
        _deepLinks.tryEmit(uri)
    }
}
