package com.iqbalwork.ramadhancamp.shared.common.preferences.domain

interface AppPreferences {
    fun scope(name: String): ScopedPreferences
}