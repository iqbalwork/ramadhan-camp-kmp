package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

data class PrayCountdownUiModel(
    val prayerName: String,
    val prayerTime: String,
    val remainingTime: String,
    val prevPrayerName: String,
    val prevPrayerTime: String,
    val nextPrayerName: String,
    val nextPrayerTime: String,
)
