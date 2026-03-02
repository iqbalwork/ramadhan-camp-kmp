package com.iqbalwork.ramadhancamp.shared.common.preferences.data

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.ScopedPreferences
import com.russhwolf.settings.Settings

class ScopedPreferencesImpl(
    private val settings: Settings,
    private val scope: String
) : ScopedPreferences {

    private fun key(key: String) = "$scope.$key"

    override fun getString(key: String, default: String) = settings.getString(key(key), default)
    override fun putString(key: String, value: String) = settings.putString(key(key), value)

    override fun getInt(key: String, default: Int) = settings.getInt(key(key), default)
    override fun putInt(key: String, value: Int) = settings.putInt(key(key), value)

    override fun getDouble(key: String, default: Double) = settings.getDouble(key(key), default)
    override fun putDouble(key: String, value: Double) = settings.putDouble(key(key), value)

    override fun getBoolean(key: String, default: Boolean) = settings.getBoolean(key(key), default)
    override fun putBoolean(key: String, value: Boolean) = settings.putBoolean(key(key), value)

    override fun getLong(key: String, default: Long) = settings.getLong(key(key), default)
    override fun putLong(key: String, value: Long) = settings.putLong(key(key), value)

    override fun remove(key: String) = settings.remove(key(key))
    override fun clear() = settings.clear()
}