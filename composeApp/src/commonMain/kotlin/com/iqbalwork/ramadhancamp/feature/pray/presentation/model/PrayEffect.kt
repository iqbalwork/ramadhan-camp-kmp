package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

import com.iqbalwork.ramadhancamp.shared.common.ui.UiEffect

sealed interface PrayEffect : UiEffect {
    /** Emitted on Android 13+ when POST_NOTIFICATIONS permission is not granted yet. */
    data object RequestNotificationPermission : PrayEffect
}
