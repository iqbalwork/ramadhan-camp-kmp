package com.iqbalwork.ramadhancamp.feature.pray.domain.usecase

import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository

fun interface ResyncPrayAlarms {
    suspend operator fun invoke()
}

internal class ResyncPrayAlarmsImpl(
    private val repo: PrayRepository
) : ResyncPrayAlarms {
    override suspend fun invoke() = repo.resyncAlarms()
}
