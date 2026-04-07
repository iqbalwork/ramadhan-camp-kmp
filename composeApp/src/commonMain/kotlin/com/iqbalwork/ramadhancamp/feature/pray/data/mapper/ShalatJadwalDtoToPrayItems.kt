package com.iqbalwork.ramadhancamp.feature.pray.data.mapper

import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatJadwalDto
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayItem
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.toPrayerDisplayName
import com.iqbalwork.ramadhancamp.shared.common.extension.padZero
import com.iqbalwork.ramadhancamp.shared.common.extension.toSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun ShalatJadwalDto.timeForKey(key: Prayers): String? = when (key) {
    Prayers.SUBUH   -> subuh
    Prayers.DZUHUR  -> dzuhur
    Prayers.ASHAR   -> ashar
    Prayers.MAGHRIB -> maghrib
    Prayers.ISYA    -> isya
}

fun ShalatJadwalDto.toPrayItems(
    selectedDate: LocalDate,
    now: LocalDateTime,
    alarmStates: Map<Prayers, Boolean>
): List<PrayItem> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val nowSeconds = now.hour * 3600 + now.minute * 60 + now.second
    val nextPrayerKey: Prayers? = if (selectedDate == today) {
        Prayers.entries
            .firstOrNull { key ->
                val time = timeForKey(key) ?: return@firstOrNull false
                time.toSeconds() > nowSeconds
            }
    } else null
    return Prayers.entries.mapNotNull { key ->
        val time = timeForKey(key) ?: return@mapNotNull null
        PrayItem(
            key = key,
            displayName = key.toPrayerDisplayName(),
            time = time,
            isNextPrayer = key == nextPrayerKey,
            isAlarmOn = alarmStates[key] ?: false,
        )
    }
}

fun ShalatJadwalDto.toPrayCountdown(now: LocalDateTime): PrayCountdown {
    val prayers = listOf(
        "Fajr"    to subuh,
        "Dhuhr"   to dzuhur,
        "Asr"     to ashar,
        "Maghrib" to maghrib,
        "Isha"    to isya,
    )

    val nowSeconds = now.hour * 3600 + now.minute * 60 + now.second

    val nextIndex = prayers.indexOfFirst { (_, time) -> time.toSeconds() > nowSeconds }
        .takeIf { it >= 0 } ?: 0

    val next = prayers[nextIndex]
    val prev = prayers[(nextIndex - 1 + prayers.size) % prayers.size]
    val afterNext = prayers[(nextIndex + 1) % prayers.size]

    val nextSeconds = next.second.toSeconds()
    val remainingSeconds = if (nextSeconds > nowSeconds) {
        (nextSeconds - nowSeconds).toLong()
    } else {
        (86400 - nowSeconds + nextSeconds).toLong()
    }

    val h = remainingSeconds / 3600
    val m = (remainingSeconds % 3600) / 60
    val s = remainingSeconds % 60
    val remainingTime = "${h.padZero()}:${m.padZero()}:${s.padZero()}"

    return PrayCountdown(
        prayerName = next.first,
        prayerTime = next.second,
        remainingTime = remainingTime,
        prevPrayerName = prev.first,
        prevPrayerTime = prev.second,
        nextPrayerName = afterNext.first,
        nextPrayerTime = afterNext.second,
    )
}
