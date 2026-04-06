package com.iqbalwork.ramadhancamp.feature.pray.data.mapper

import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatJadwalDto
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayItem
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

// Returns the prayer time string for a given internal prayer key
fun ShalatJadwalDto.timeForKey(key: String): String? = when (key) {
    "imsak"   -> imsak
    "subuh"   -> subuh
    "terbit"  -> terbit
    "dzuhur"  -> dzuhur
    "ashar"   -> ashar
    "maghrib" -> maghrib
    "isya"    -> isya
    else      -> null
}

private fun String.toTotalSeconds(): Int {
    val parts = split(":")
    return parts[0].toInt() * 3600 + parts[1].toInt() * 60
}

// Builds PrayItem list for a given date and current time
fun ShalatJadwalDto.toPrayItems(
    selectedDate: LocalDate,
    today: LocalDate,
    now: LocalDateTime,
    alarmStates: Map<String, Boolean>
): List<PrayItem> {
    // Prayer order definitions: key, displayName, canHaveAlarm
    val definitions = listOf(
        Triple("imsak",   "Imsak",   true),
        Triple("subuh",   "Fajr",    true),
        Triple("terbit",  "Sunrise", false),
        Triple("dzuhur",  "Dhuhr",   true),
        Triple("ashar",   "Asr",     true),
        Triple("maghrib", "Maghrib", true),
        Triple("isya",    "Isha",    true),
    )

    val nowSeconds = now.hour * 3600 + now.minute * 60 + now.second

    // Determine next prayer key (only relevant for today)
    val nextPrayerKey: String? = if (selectedDate == today) {
        definitions
            .filter { (key, _, canAlarm) -> canAlarm && key != "imsak" } // Imsak is not a shalat
            .firstOrNull { (key, _, _) ->
                val time = timeForKey(key) ?: return@firstOrNull false
                time.toTotalSeconds() > nowSeconds
            }?.first
    } else null

    return definitions.map { (key, displayName, canHaveAlarm) ->
        val time = timeForKey(key) ?: "00:00"

        val isPast = when {
            selectedDate < today -> true
            selectedDate > today -> false
            else -> time.toTotalSeconds() <= nowSeconds
        }

        PrayItem(
            key = key,
            displayName = displayName,
            time = time,
            isNextPrayer = key == nextPrayerKey,
            isPast = isPast,
            isAlarmOn = alarmStates[key] ?: false,
            canSetAlarm = canHaveAlarm && !isPast
        )
    }
}

// Builds PrayCountdown — called every second for today's schedule
fun ShalatJadwalDto.toPrayCountdown(now: LocalDateTime): PrayCountdown {
    // Only actual prayer times (not Sunrise) for countdown logic
    val prayers = listOf(
        "imsak"   to imsak,
        "Fajr"    to subuh,
        "Dhuhr"   to dzuhur,
        "Asr"     to ashar,
        "Maghrib" to maghrib,
        "Isha"    to isya,
    )

    val nowSeconds = now.hour * 3600 + now.minute * 60 + now.second

    val nextIndex = prayers.indexOfFirst { (_, time) -> time.toTotalSeconds() > nowSeconds }
        .takeIf { it >= 0 } ?: 0  // wrap to first prayer next day

    val next = prayers[nextIndex]
    val prev = prayers[(nextIndex - 1 + prayers.size) % prayers.size]
    val afterNext = prayers[(nextIndex + 1) % prayers.size]

    val nextSeconds = next.second.toTotalSeconds()
    val remainingSeconds = if (nextSeconds > nowSeconds) {
        (nextSeconds - nowSeconds).toLong()
    } else {
        (86400 - nowSeconds + nextSeconds).toLong()  // crossed midnight
    }

    return PrayCountdown(
        prayerName = next.first,
        prayerTime = next.second,
        remainingSeconds = remainingSeconds,
        prevPrayerName = prev.first,
        prevPrayerTime = prev.second,
        nextPrayerName = afterNext.first,
        nextPrayerTime = afterNext.second,
    )
}
