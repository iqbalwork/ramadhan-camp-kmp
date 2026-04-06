package com.iqbalwork.ramadhancamp.feature.pray.domain.model

data class PrayCountdown(
    val prayerName: String,       // "Asr"
    val prayerTime: String,       // "14:58"
    val remainingSeconds: Long,
    val prevPrayerName: String,   // shown left in card footer
    val prevPrayerTime: String,
    val nextPrayerName: String,   // shown right in card footer
    val nextPrayerTime: String,
)
