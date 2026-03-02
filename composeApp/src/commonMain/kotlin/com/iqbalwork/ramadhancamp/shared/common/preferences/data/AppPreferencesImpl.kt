package com.iqbalwork.ramadhancamp.shared.common.preferences.data

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.ScopedPreferences
import com.russhwolf.settings.Settings

class AppPreferencesImpl(private val settings: Settings) : AppPreferences {
    override fun scope(name: String): ScopedPreferences =
        ScopedPreferencesImpl(settings, name)
}