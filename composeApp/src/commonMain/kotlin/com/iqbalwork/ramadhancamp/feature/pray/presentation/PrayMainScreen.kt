package com.iqbalwork.ramadhancamp.feature.pray.presentation

import androidx.compose.animation.AnimatedContent
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
import com.iqbalwork.ramadhancamp.shared.common.ui.components.error.ErrorEmptyState
import com.iqbalwork.ramadhancamp.shared.common.ui.components.error.RamadhanErrorEmptyState
import com.iqbalwork.ramadhancamp.shared.common.ui.components.error.toErrorEmptyState
import com.iqbalwork.ramadhancamp.shared.common.ui.components.loading.Loader
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import com.iqbalwork.ramadhancamp.shared.common.utils.AppError
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private sealed interface AnimateContentState {
    data object Loading : AnimateContentState
    class Error(val error: AppError) : AnimateContentState
    data object NoLocation : AnimateContentState
    data object Success : AnimateContentState
}

@Composable
fun PrayMainScreen() {
    val viewModel: PrayViewModel = rememberViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    PrayContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        action = viewModel.rememberDispatch()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayContent(
    modifier: Modifier,
    state: PrayState,
    action: (PrayEvent) -> Unit
) {
    val colors = RamadhanTheme.colors

    Box(
        modifier = modifier
            .background(colors.bgPrimary)
    ) {

        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = when {
                state.isLoading -> AnimateContentState.Loading
                state.error != null -> AnimateContentState.Error(state.error)
                !state.hasLocation -> AnimateContentState.NoLocation
                else -> AnimateContentState.Success
            }
        ) { targetState ->
            when(targetState) {
                is AnimateContentState.NoLocation -> NoLocationPlaceholder()

                is AnimateContentState.Loading -> Loader(modifier = Modifier.fillMaxSize())

                is AnimateContentState.Error -> RamadhanErrorEmptyState(
                    modifier = Modifier.fillMaxSize(),
                    errorEmptyState = targetState.error.toErrorEmptyState(),
                    onButtonClick = { action(PrayEvent.RetryLoadSchedule) }
                )

                is AnimateContentState.Success -> {
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
