package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector

data class PrayItemUiModel(
    val key: String,
    val displayName: String,
    val time: String,
    val icon: ImageVector,
    val isNextPrayer: Boolean,
    val isPast: Boolean,
    val isAlarmOn: Boolean,
    val canSetAlarm: Boolean
)
