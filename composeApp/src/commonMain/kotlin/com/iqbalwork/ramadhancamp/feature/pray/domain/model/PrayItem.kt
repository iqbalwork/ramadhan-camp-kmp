package com.iqbalwork.ramadhancamp.feature.pray.domain.model

data class PrayItem(
    val key: Prayers,          // "subuh", "dzuhur", etc. — used as alarm pref key
    val displayName: String,  // "Fajr", "Dhuhr", etc. — shown in UI
    val time: String,         // "04:25"
    val isNextPrayer: Boolean,
    val isAlarmOn: Boolean,
)
