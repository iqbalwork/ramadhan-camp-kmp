package com.iqbalwork.ramadhancamp.feature.pray.presentation.model

import com.iqbalwork.ramadhancamp.shared.common.ui.UiEvent
import kotlinx.datetime.LocalDate

sealed interface PrayEvent : UiEvent {
    data object Load : PrayEvent
    data class DateSelected(val date: LocalDate) : PrayEvent
    data class ToggleAlarm(val prayerKey: String, val enabled: Boolean) : PrayEvent
    data object OpenDatePicker : PrayEvent
    data object CloseDatePicker : PrayEvent
}
