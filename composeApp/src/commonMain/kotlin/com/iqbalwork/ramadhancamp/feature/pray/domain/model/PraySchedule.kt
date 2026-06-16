package com.iqbalwork.ramadhancamp.feature.pray.domain.model

import kotlinx.datetime.LocalDate

data class PraySchedule(
    val date: LocalDate,
    val prayers: List<PrayItem>
)
