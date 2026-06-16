package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers

data class PrayItemUiModel(
    val key: Prayers,
    val displayName: String,
    val time: String,
    val icon: ImageVector,
    val isNextPrayer: Boolean,
    val isAlarmOn: Boolean,
)
