package com.iqbalwork.ramadhancamp.feature.pray.presentation.mapper

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayItem
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayCountdownUiModel
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayItemUiModel

fun PrayItem.toUiModel(): PrayItemUiModel = PrayItemUiModel(
    key = key,
    displayName = displayName,
    time = time,
    icon = when (key) {
        "imsak"   -> Icons.Default.NightsStay
        "subuh"   -> Icons.Default.Bedtime
        "terbit"  -> Icons.Default.WbSunny
        "dzuhur"  -> Icons.Default.LightMode
        "ashar"   -> Icons.Default.WbTwilight
        "maghrib" -> Icons.Default.WbTwilight
        "isya"    -> Icons.Default.Nightlight
        else      -> Icons.Default.LightMode
    },
    isNextPrayer = isNextPrayer,
    isPast = isPast,
    isAlarmOn = isAlarmOn,
    canSetAlarm = canSetAlarm
)

fun PrayCountdown.toUiModel(): PrayCountdownUiModel {
    val h = remainingSeconds / 3600
    val m = (remainingSeconds % 3600) / 60
    val s = remainingSeconds % 60
    return PrayCountdownUiModel(
        prayerName = prayerName,
        prayerTime = prayerTime,
        hours = h.toString().padStart(2, '0'),
        minutes = m.toString().padStart(2, '0'),
        seconds = s.toString().padStart(2, '0'),
        prevPrayerName = prevPrayerName,
        prevPrayerTime = prevPrayerTime,
        nextPrayerName = nextPrayerName,
        nextPrayerTime = nextPrayerTime,
    )
}
