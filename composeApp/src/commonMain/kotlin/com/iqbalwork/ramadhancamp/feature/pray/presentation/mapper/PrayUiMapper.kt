package com.iqbalwork.ramadhancamp.feature.pray.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbTwilight
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayItem
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayCountdownUiModel
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayItemUiModel

import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers

fun PrayItem.toUiModel(): PrayItemUiModel = PrayItemUiModel(
    key = key,
    displayName = displayName,
    time = time,
    icon = when (key) {
        Prayers.SUBUH   -> Icons.Default.Bedtime
        Prayers.DZUHUR  -> Icons.Default.LightMode
        Prayers.ASHAR   -> Icons.Default.WbTwilight
        Prayers.MAGHRIB -> Icons.Default.WbTwilight
        Prayers.ISYA    -> Icons.Default.Nightlight
    },
    isNextPrayer = isNextPrayer,
    isAlarmOn = isAlarmOn,
)

fun PrayCountdown.toUiModel(): PrayCountdownUiModel {
    return PrayCountdownUiModel(
        prayerName = prayerName,
        prayerTime = prayerTime,
        remainingTime = remainingTime,
        prevPrayerName = prevPrayerName,
        prevPrayerTime = prevPrayerTime,
        nextPrayerName = nextPrayerName,
        nextPrayerTime = nextPrayerTime,
    )
}
