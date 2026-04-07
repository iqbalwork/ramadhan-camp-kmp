package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import com.iqbalwork.ramadhancamp.feature.pray.presentation.mapper.toUiModel
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayEffect
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayEvent
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import com.iqbalwork.ramadhancamp.shared.common.utils.AppError
import com.iqbalwork.ramadhancamp.shared.common.utils.toAppError
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PrayViewModel(
    navController: NavigationManager,
    private val prayRepository: PrayRepository,
) : BaseViewModel<Unit, PrayState, PrayEvent, PrayEffect>(
    params = Unit,
    initialState = PrayState(),
    navigationManager = navController,
    resultKeys = arrayOf(),
) {
    private var hasLoadTodaySchedule = false

    init {
        viewModelScope.apply {
            launch { initPraySchedule() }
            launch { initNextPraySchedule() }
        }
    }

    private suspend fun initPraySchedule() {
        prayRepository.lastCity
            .collect {
                updateState { copy(isLoading = true) }
                if (it == null) {
                    updateState { copy(isLoading = false, hasLocation = false) }
                    return@collect
                }
                loadTodaySchedule()
            }
    }

    private suspend fun initNextPraySchedule() {
        prayRepository.countdown.collect { countdown ->
            updateState { copy(countdown = countdown.toUiModel()) }
        }
    }

    override fun handleEvent(event: PrayEvent) {
        when (event) {
            is PrayEvent.DateSelected -> viewModelScope.launch { loadScheduleForDate(event.date) }
            is PrayEvent.ToggleAlarm -> viewModelScope.launch { onToggleAlarm(event.prayerKey, event.enabled) }
            PrayEvent.OpenDatePicker -> updateState { copy(isDatePickerVisible = true) }
            PrayEvent.CloseDatePicker -> updateState { copy(isDatePickerVisible = false) }
            PrayEvent.RetryLoadSchedule -> viewModelScope.launch {
                if (hasLoadTodaySchedule) loadScheduleForDate(state.value.selectedDate)
                else loadTodaySchedule()
            }
        }
    }

    private suspend fun loadTodaySchedule() {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        updateState { copy(error = null, selectedDate = today) }
        prayRepository.loadSchedule(today)
            .onSuccess { schedule ->
                updateState { copy(isLoading = false, prayers = schedule.prayers.map { it.toUiModel() }, hasLocation = true) }
                prayRepository.resyncAlarms()
                hasLoadTodaySchedule = true
            }
            .onFailure { error ->
                val appError = error.toAppError()
                if (appError is AppError.ServerError && appError.httpCode == 404) {
                    updateState { copy(isLoading = false, hasLocation = false) }
                } else {
                    updateState { copy(isLoading = false, error = appError) }
                }
            }
    }

    private suspend fun loadScheduleForDate(date: LocalDate) {
        updateState { copy(isLoading = true, error = null, selectedDate = date, isDatePickerVisible = false) }
        prayRepository.loadSchedule(date)
            .onSuccess { schedule ->
                updateState { copy(isLoading = false, prayers = schedule.prayers.map { it.toUiModel() }) }
            }
            .onFailure { error ->
                updateState { copy(isLoading = false, error = error.toAppError()) }
            }
    }

    private suspend fun onToggleAlarm(prayerKey: Prayers, enabled: Boolean) {
        prayRepository.toggleAlarm(prayerKey, enabled)
            .onSuccess {
                updateState {
                    copy(prayers = prayers.map { item ->
                        if (item.key == prayerKey) item.copy(isAlarmOn = enabled) else item
                    })
                }
            }
            .onFailure {
                //TODO throw snackbar
            }
    }
}
        