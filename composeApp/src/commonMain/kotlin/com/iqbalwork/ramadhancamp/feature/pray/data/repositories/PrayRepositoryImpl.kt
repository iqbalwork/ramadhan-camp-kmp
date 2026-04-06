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
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import com.tweener.alarmee.Alarmee
import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AndroidNotificationConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class PrayRepositoryImpl(
    private val remoteDatasource: PrayRemoteDatasource,
    private val prayPreferences: PrayPreferences,
    private val homePreferences: HomePreferences,
    private val alarmeService: AlarmeeScheduler,
) : PrayRepository {

    // Month cache: (month, year) → full monthly schedule
    private val monthCache = mutableMapOf<Pair<Int, Int>, ShalatScheduleDto>()

    // Today's schedule drives the countdown ticker
    private val todayScheduleFlow = MutableStateFlow<ShalatScheduleDto?>(null)

    override val countdown: Flow<PrayCountdown> = todayScheduleFlow
        .flatMapLatest { schedule ->
            flow {
                while (true) {
                    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    schedule?.data?.jadwal
                        ?.find { it.tanggal == now.dayOfMonth }
                        ?.let { emit(it.toPrayCountdown(now)) }
                    delay(1000L)
                }
            }
        }

    override suspend fun loadSchedule(date: LocalDate): Result<PraySchedule> = runCatching {
        if (homePreferences.lastCityStateFlow.value.isNullOrEmpty()) error("No location set")

        val schedule = getOrFetchMonthSchedule(date.monthNumber, date.year)
            ?: error("No schedule available — location not set")

        // Cache today's month for the countdown ticker
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.monthNumber == today.monthNumber && date.year == today.year) {
            todayScheduleFlow.value = schedule
        }

        val jadwal = schedule.data.jadwal.find { it.tanggal == date.dayOfMonth }
            ?: error("No jadwal for day ${date.dayOfMonth}")

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val alarmStates = prayPreferences.allAlarmKeys.associateWith { prayPreferences.getAlarmState(it) }

        PraySchedule(
            date = date,
            prayers = jadwal.toPrayItems(
                selectedDate = date,
                today = today,
                now = now,
                alarmStates = alarmStates
            )
        )
    }

    override suspend fun toggleAlarm(prayerKey: String, enabled: Boolean): Result<Unit> = runCatching {
        prayPreferences.setAlarmState(prayerKey, enabled)

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

        if (enabled) {
            // Schedule 10-day batch
            for (dayOffset in 0..9) {
                val targetDate = today.plus(dayOffset, DateTimeUnit.DAY)
                val schedule = getOrFetchMonthSchedule(targetDate.monthNumber, targetDate.year) ?: continue
                val jadwal = schedule.data.jadwal.find { it.tanggal == targetDate.dayOfMonth } ?: continue
                val timeStr = jadwal.timeForKey(prayerKey) ?: continue
                val (hour, minute) = timeStr.split(":").map { it.toInt() }

                val scheduledDateTime = LocalDateTime(
                    year = targetDate.year,
                    monthNumber = targetDate.monthNumber,
                    dayOfMonth = targetDate.dayOfMonth,
                    hour = hour,
                    minute = minute,
                    second = 0,
                    nanosecond = 0
                )

                // Only schedule future alarms
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
            // Cancel a generous range to ensure all stale alarms are cleared
            for (dayOffset in -1..30) {
                val targetDate = today.plus(dayOffset, DateTimeUnit.DAY)
                alarmeService.cancel(uuid = "pray-$prayerKey-$targetDate")
            }
        }
    }

    override suspend fun resyncAlarms() {
        prayPreferences.allAlarmKeys.forEach { key ->
            if (prayPreferences.getAlarmState(key)) {
                toggleAlarm(key, enabled = true)  // re-schedule fresh 10-day window
            }
        }
    }

    private suspend fun getOrFetchMonthSchedule(month: Int, year: Int): ShalatScheduleDto? {
        val key = month to year
        monthCache[key]?.let { return it }

        val province = homePreferences.lastProvince ?: return null
        val city = homePreferences.lastCity ?: return null

        return remoteDatasource.getShalatSchedule(province, city, month, year)
            .getOrNull()
            ?.also { monthCache[key] = it }
    }
}

// Helper: convert prayer key to display name
private fun String.toPrayerDisplayName(): String = when (this) {
    "imsak"   -> "Imsak"
    "subuh"   -> "Fajr"
    "dzuhur"  -> "Dhuhr"
    "ashar"   -> "Asr"
    "maghrib" -> "Maghrib"
    "isya"    -> "Isha"
    else      -> this.replaceFirstChar { it.uppercase() }
}
