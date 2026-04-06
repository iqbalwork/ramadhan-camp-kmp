package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class PrayState(
    val isLoading: Boolean = false,
    val hasLocation: Boolean = false,
    val city: String = "",
    val selectedDate: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val countdown: PrayCountdownUiModel? = null,
    val prayers: List<PrayItemUiModel> = emptyList(),
    val isDatePickerVisible: Boolean = false,
    val error: String? = null
)
