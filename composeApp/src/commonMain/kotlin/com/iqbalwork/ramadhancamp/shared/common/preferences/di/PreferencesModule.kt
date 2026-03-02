package com.iqbalwork.ramadhancamp.shared.common.preferences.di

import com.iqbalwork.ramadhancamp.shared.common.preferences.data.AppPreferencesImpl
import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val preferencesModule = module {
    single<Settings> { Settings() }
    single<AppPreferences> { AppPreferencesImpl(get()) }
}