package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.pray.presentation.components.NextPrayerCard
import com.iqbalwork.ramadhancamp.feature.pray.presentation.components.NoLocationPlaceholder
import com.iqbalwork.ramadhancamp.feature.pray.presentation.components.PrayHeader
import com.iqbalwork.ramadhancamp.feature.pray.presentation.components.PrayerRowItem
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayEvent
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayState
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun PrayMainScreen() {
    val vm: PrayViewModel = rememberViewModel()
    val state by vm.state.collectAsStateWithLifecycle()
    PrayContent(state = state, action = vm.rememberDispatch())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayContent(
    state: PrayState,
    action: (PrayEvent) -> Unit
) {
    val colors = RamadhanTheme.colors

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.bgPrimary)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        when {
            !state.hasLocation -> NoLocationPlaceholder()

            state.isLoading && state.prayers.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colors.accentPrimary
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    item {
                        PrayHeader(
                            city = state.city,
                            selectedDate = state.selectedDate,
                            onCalendarClick = { action(PrayEvent.OpenDatePicker) }
                        )
                    }

                    state.countdown?.let { countdown ->
                        item {
                            NextPrayerCard(
                                countdown = countdown,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }

                    item {
                        Spacer(Modifier.height(16.dp))
                    }
                    items(state.prayers, key = { it.key }) { prayer ->
                        PrayerRowItem(
                            item = prayer,
                            onAlarmToggle = { key, enabled ->
                                action(PrayEvent.ToggleAlarm(key, enabled))
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        if (state.isDatePickerVisible) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { action(PrayEvent.CloseDatePicker) },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            action(PrayEvent.DateSelected(date))
                        }
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { action(PrayEvent.CloseDatePicker) }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}
