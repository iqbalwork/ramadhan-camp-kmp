package com.iqbalwork.ramadhancamp.feature.pray.data.repositories

import com.iqbalwork.ramadhancamp.feature.home.data.datasource.HomePreferences
import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatScheduleDto
import com.iqbalwork.ramadhancamp.feature.pray.data.datasource.PrayPreferences
import com.iqbalwork.ramadhancamp.feature.pray.data.datasource.PrayRemoteDatasource
import com.iqbalwork.ramadhancamp.feature.pray.data.mapper.timeForKey
import com.iqbalwork.ramadhancamp.feature.pray.data.mapper.toPrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.data.mapper.toPrayItems
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PrayCountdown
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.PraySchedule
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.toPrayerDisplayName
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import com.tweener.alarmee.Alarmee
import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AndroidNotificationConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

@OptIn(ExperimentalCoroutinesApi::class)
class PrayRepositoryImpl(
    private val remoteDatasource: PrayRemoteDatasource,
    private val prayPreferences: PrayPreferences,
    private val homePreferences: HomePreferences,
    private val alarmeService: AlarmeeScheduler,
) : PrayRepository {

    override val lastCity: StateFlow<String?> = homePreferences.lastCityStateFlow()
    private val todayScheduleFlow = MutableStateFlow<ShalatScheduleDto?>(null)

    override val countdown: Flow<PrayCountdown> = todayScheduleFlow
        .flatMapLatest { schedule ->
            flow {
                while (true) {
                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    schedule?.data?.jadwal
                        ?.find { it.tanggal == now.day }
                        ?.let { emit(it.toPrayCountdown(now)) }
                    val secondsUntilNextMinute = 60 - now.second
                    delay(secondsUntilNextMinute * 1000L)
                }
            }
        }

    override suspend fun loadSchedule(date: LocalDate): Result<PraySchedule> = runCatching {
        val schedule = getOrFetchMonthSchedule(date.month.number, date.year).getOrThrow()

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.month.number == today.month.number && date.year == today.year) {
            todayScheduleFlow.value = schedule
        }

        val jadwal = schedule?.data?.jadwal?.find { it.tanggal == date.day }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val alarmStates = prayPreferences.getAllAlarmsState()

        PraySchedule(
            date = date,
            prayers = jadwal!!.toPrayItems(
                selectedDate = date,
                now = now,
                alarmStates = alarmStates
            )
        )
    }

    override suspend fun toggleAlarm(prayerKey: Prayers, enabled: Boolean): Result<Unit> = runCatching {
        prayPreferences.setAlarmState(prayerKey, enabled)

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (enabled) {
            for (dayOffset in 0..9) {
                val targetDate = today.plus(dayOffset, DateTimeUnit.DAY)
                val schedule = getOrFetchMonthSchedule(targetDate.month.number, targetDate.year).getOrThrow() ?: continue
                val jadwal = schedule.data.jadwal.find { it.tanggal == targetDate.day } ?: continue
                val timeStr = jadwal.timeForKey(prayerKey) ?: continue
                val (hour, minute) = timeStr.split(":").map { it.toInt() }

                val scheduledDateTime = LocalDateTime(
                    year = targetDate.year,
                    month = targetDate.month.number,
                    day = targetDate.day,
                    hour = hour,
                    minute = minute,
                )

                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                if (scheduledDateTime > now) {
                    alarmeService.schedule(
                        alarmee = Alarmee(
                            uuid = "pray-$prayerKey-$targetDate",
                            notificationTitle = prayerKey.toPrayerDisplayName(),
                            notificationBody = "It's time for ${prayerKey.toPrayerDisplayName()} prayer",
                            scheduledDateTime = scheduledDateTime,
                            androidNotificationConfiguration = AndroidNotificationConfiguration(
                                channelId = "pray_notifications"
                            )
                        )
                    )
                }
            }
        } else {
            for (dayOffset in -1..30) {
                val targetDate = today.plus(dayOffset, DateTimeUnit.DAY)
                alarmeService.cancel(uuid = "pray-$prayerKey-$targetDate")
            }
        }
    }

    override suspend fun resyncAlarms() {
        Prayers.entries.forEach { key ->
            if (prayPreferences.getAlarmState(key)) {
                toggleAlarm(key, enabled = true)
            }
        }
    }

    private suspend fun getOrFetchMonthSchedule(month: Int, year: Int): Result<ShalatScheduleDto?> = runCatching {
        homePreferences.getShalatSchedule(month, year)?.let { return Result.success(it) }

        val province = homePreferences.lastProvince ?: return Result.success(null)
        val city = homePreferences.lastCity ?: return Result.success(null)

        return remoteDatasource.getShalatSchedule(province, city, month, year)
            .onSuccess { homePreferences.saveShalatSchedule(month, year, it) }
    }
}
