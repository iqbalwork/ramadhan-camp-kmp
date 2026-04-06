package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

data class PrayCountdownUiModel(
    val prayerName: String,
    val prayerTime: String,
    val hours: String,      // "01" (zero-padded)
    val minutes: String,    // "24"
    val seconds: String,    // "05"
    val prevPrayerName: String,
    val prevPrayerTime: String,
    val nextPrayerName: String,
    val nextPrayerTime: String,
)
