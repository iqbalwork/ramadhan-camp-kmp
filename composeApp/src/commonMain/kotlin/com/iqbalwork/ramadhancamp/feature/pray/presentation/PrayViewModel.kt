package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.home.data.datasource.HomePreferences
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.LoadPraySchedule
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.ResyncPrayAlarms
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.TogglePrayAlarm
import com.iqbalwork.ramadhancamp.feature.pray.presentation.mapper.toUiModel
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayEffect
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayEvent
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PrayViewModel(
    navController: NavigationManager,
    private val prayRepository: PrayRepository,
    private val loadPraySchedule: LoadPraySchedule,
    private val togglePrayAlarm: TogglePrayAlarm,
    private val resyncPrayAlarms: ResyncPrayAlarms,
    private val homePreferences: HomePreferences,
) : BaseViewModel<Unit, PrayState, PrayEvent, PrayEffect>(
    params = Unit,
    initialState = PrayState(),
    navigationManager = navController,
    resultKeys = arrayOf(),
) {

    init {
        viewModelScope.launch {
            homePreferences.lastCityFlow.flow.collectLatest { city ->
                val hasLocation = !city.isNullOrBlank()
                updateState { copy(hasLocation = hasLocation, city = city ?: "") }
                if (hasLocation) handleEvent(PrayEvent.Load)
            }
        }

        // Collect second-by-second countdown
        viewModelScope.launch {
            prayRepository.countdown.collect { countdown ->
                updateState { copy(countdown = countdown.toUiModel()) }
            }
        }
    }

    override fun handleEvent(event: PrayEvent) {
        when (event) {
            PrayEvent.Load -> loadTodaySchedule()
            is PrayEvent.DateSelected -> loadScheduleForDate(event.date)
            is PrayEvent.ToggleAlarm -> onToggleAlarm(event.prayerKey, event.enabled)
            PrayEvent.OpenDatePicker -> updateState { copy(isDatePickerVisible = true) }
            PrayEvent.CloseDatePicker -> updateState { copy(isDatePickerVisible = false) }
        }
    }

    private fun loadTodaySchedule() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null, selectedDate = today) }
            loadPraySchedule(today)
                .onSuccess { schedule ->
                    updateState { copy(isLoading = false, prayers = schedule.prayers.map { it.toUiModel() }) }
                    resyncPrayAlarms()
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun loadScheduleForDate(date: kotlinx.datetime.LocalDate) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null, selectedDate = date, isDatePickerVisible = false) }
            loadPraySchedule(date)
                .onSuccess { schedule ->
                    updateState { copy(isLoading = false, prayers = schedule.prayers.map { it.toUiModel() }) }
                }
                .onFailure { error ->
                    updateState { copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun onToggleAlarm(prayerKey: String, enabled: Boolean) {
        viewModelScope.launch {
            togglePrayAlarm(prayerKey, enabled)
                .onSuccess {
                    updateState {
                        copy(prayers = prayers.map { item ->
                            if (item.key == prayerKey) item.copy(isAlarmOn = enabled) else item
                        })
                    }
                }
                .onFailure { /* optionally show error snackbar */ }
        }
    }
}
