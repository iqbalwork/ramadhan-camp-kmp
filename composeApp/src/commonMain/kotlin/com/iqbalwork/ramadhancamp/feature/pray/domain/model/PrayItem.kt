package com.iqbalwork.ramadhancamp.feature.pray.domain.model

data class PrayItem(
    val key: String,          // "subuh", "dzuhur", etc. — used as alarm pref key
    val displayName: String,  // "Fajr", "Dhuhr", etc. — shown in UI
    val time: String,         // "04:25"
    val isNextPrayer: Boolean,
    val isPast: Boolean,
    val isAlarmOn: Boolean,
    val canSetAlarm: Boolean  // false for Sunrise; false if isPast
)
