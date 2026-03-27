package com.iqbalwork.ramadhancamp.feature.home.data.mapper

import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatJadwalDto
import com.iqbalwork.ramadhancamp.feature.home.domain.model.NextPrayer
import com.iqbalwork.ramadhancamp.shared.common.extension.toMinutes
import kotlinx.datetime.LocalDateTime

fun ShalatJadwalDto.nextPrayer(now: LocalDateTime): NextPrayer {
    val prayers = listOf(
        "Subuh" to subuh,
        "Dzuhur" to dzuhur,
        "Ashar" to ashar,
        "Maghrib" to maghrib,
        "Isya" to isya,
    )

    val nowMinutes = now.hour * 60 + now.minute


    val next = prayers.firstOrNull { (_, time) -> time.toMinutes() > nowMinutes }
        ?: prayers.first() // wrap to Subuh next day

    val prayerMinutes = next.second.toMinutes()
    val remaining = if (prayerMinutes > nowMinutes) {
        prayerMinutes - nowMinutes
    } else {
        (24 * 60 - nowMinutes) + prayerMinutes // crossed midnight
    }

    return NextPrayer(name = next.first, time = next.second, remainingMinutes = remaining)
}