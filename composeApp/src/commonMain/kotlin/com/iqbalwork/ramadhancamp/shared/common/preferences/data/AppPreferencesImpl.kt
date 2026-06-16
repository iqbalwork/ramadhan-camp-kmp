package com.iqbalwork.ramadhancamp.shared.common.preferences.data

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.ScopedPreferences
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope

class AppPreferencesImpl(private val settings: ObservableSettings, private val coroutineScope: CoroutineScope) : AppPreferences {
    override fun scope(name: String): ScopedPreferences = ScopedPreferencesImpl(settings, name, coroutineScope)
}