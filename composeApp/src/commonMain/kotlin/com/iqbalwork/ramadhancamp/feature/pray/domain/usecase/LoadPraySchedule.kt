package com.iqbalwork.ramadhancamp.feature.pray.domain.usecase

import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PraySchedule
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import kotlinx.datetime.LocalDate

fun interface LoadPraySchedule {
    suspend operator fun invoke(date: LocalDate): Result<PraySchedule>
}

internal class LoadPrayScheduleImpl(
    private val repo: PrayRepository
) : LoadPraySchedule {
    override suspend fun invoke(date: LocalDate): Result<PraySchedule> = repo.loadSchedule(date)
}
