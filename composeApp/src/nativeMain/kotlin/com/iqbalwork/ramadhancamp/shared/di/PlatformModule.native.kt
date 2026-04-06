package com.iqbalwork.ramadhancamp.shared.di

import com.tweener.alarmee.AlarmeeScheduler
import com.tweener.alarmee.AlarmeeSchedulerIos
import com.tweener.alarmee.configuration.AlarmeeIosPlatformConfiguration
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<AlarmeeScheduler> {
        AlarmeeSchedulerIos(configuration = AlarmeeIosPlatformConfiguration)
    }
}