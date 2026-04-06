package com.iqbalwork.ramadhancamp.feature.pray.domain.repository

import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PraySchedule
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface PrayRepository {
    val countdown: Flow<PrayCountdown>
    suspend fun loadSchedule(date: LocalDate): Result<PraySchedule>
    suspend fun toggleAlarm(prayerKey: String, enabled: Boolean): Result<Unit>
    suspend fun resyncAlarms()
}
