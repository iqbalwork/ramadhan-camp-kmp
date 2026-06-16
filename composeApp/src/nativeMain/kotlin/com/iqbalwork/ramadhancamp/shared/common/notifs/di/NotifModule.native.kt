package com.iqbalwork.ramadhancamp.shared.common.notifs.di

import com.tweener.alarmee.AlarmeeService
import com.tweener.alarmee.configuration.AlarmeeIosPlatformConfiguration
import com.tweener.alarmee.createAlarmeeService
import org.koin.core.module.Module
import org.koin.dsl.module

actual val notifModule: Module = module {
    single<AlarmeeService> {
        createAlarmeeService().apply {
            initialize(platformConfiguration = AlarmeeIosPlatformConfiguration)
        }
    }
}