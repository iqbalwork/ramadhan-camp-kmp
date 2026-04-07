package com.iqbalwork.ramadhancamp.feature.pray.domain.repository

import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PraySchedule
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDate

interface PrayRepository {
    val lastCity: StateFlow<String?>
    val countdown: Flow<PrayCountdown>
    suspend fun loadSchedule(date: LocalDate): Result<PraySchedule>
    suspend fun toggleAlarm(prayerKey: Prayers, enabled: Boolean): Result<Unit>
    suspend fun resyncAlarms()
}
