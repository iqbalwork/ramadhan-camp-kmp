package com.iqbalwork.ramadhancamp.feature.pray.domain.usecase

import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository

fun interface TogglePrayAlarm {
    suspend operator fun invoke(prayerKey: String, enabled: Boolean): Result<Unit>
}

internal class TogglePrayAlarmImpl(
    private val repo: PrayRepository
) : TogglePrayAlarm {
    override suspend fun invoke(prayerKey: String, enabled: Boolean): Result<Unit> =
        repo.toggleAlarm(prayerKey, enabled)
}
