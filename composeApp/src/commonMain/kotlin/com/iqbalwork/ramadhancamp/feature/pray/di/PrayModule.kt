package com.iqbalwork.ramadhancamp.feature.pray.di

import com.iqbalwork.ramadhancamp.feature.pray.data.datasource.PrayPreferences
import com.iqbalwork.ramadhancamp.feature.pray.data.datasource.PrayRemoteDatasource
import com.iqbalwork.ramadhancamp.feature.pray.data.repositories.PrayRepositoryImpl
import com.iqbalwork.ramadhancamp.feature.pray.domain.repository.PrayRepository
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.LoadPraySchedule
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.LoadPrayScheduleImpl
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.ResyncPrayAlarms
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.ResyncPrayAlarmsImpl
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.TogglePrayAlarm
import com.iqbalwork.ramadhancamp.feature.pray.domain.usecase.TogglePrayAlarmImpl
import com.iqbalwork.ramadhancamp.feature.pray.presentation.PrayViewModel
import com.iqbalwork.ramadhancamp.shared.common.navigation.BackStackNode
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabState
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

val prayModule = module {
    factoryOf(::PrayRemoteDatasource)
    singleOf(::PrayPreferences)
    factoryOf(::PrayRepositoryImpl) bind PrayRepository::class

    factory<LoadPraySchedule> { LoadPrayScheduleImpl(get()) }
    factory<TogglePrayAlarm> { TogglePrayAlarmImpl(get()) }
    factory<ResyncPrayAlarms> { ResyncPrayAlarmsImpl(get()) }

    viewModel { params ->
        PrayViewModel(
            navController = get<NavigationManager> {
                parametersOf(
                    params.get<BackStackNode>(),
                    params.get<TabState>(),
                )
            },
            prayRepository = get(),
            loadPraySchedule = get(),
            togglePrayAlarm = get(),
            resyncPrayAlarms = get(),
            homePreferences = get(),
        )
    }
}
