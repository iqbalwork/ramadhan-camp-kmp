package com.iqbalwork.ramadhancamp.shared.common.preferences.data

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.ScopedPreferences
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.getBooleanOrNullStateFlow
import com.russhwolf.settings.coroutines.getBooleanStateFlow
import com.russhwolf.settings.coroutines.getDoubleOrNullStateFlow
import com.russhwolf.settings.coroutines.getDoubleStateFlow
import com.russhwolf.settings.coroutines.getIntOrNullStateFlow
import com.russhwolf.settings.coroutines.getIntStateFlow
import com.russhwolf.settings.coroutines.getLongOrNullStateFlow
import com.russhwolf.settings.coroutines.getLongStateFlow
import com.russhwolf.settings.coroutines.getStringOrNullStateFlow
import com.russhwolf.settings.coroutines.getStringStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalSettingsApi::class)
class ScopedPreferencesImpl(
    private val settings: ObservableSettings,
    private val scope: String,
    private val coroutineScope: CoroutineScope
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

    override fun getStringStateFlow(key: String, default: String): StateFlow<String> = settings.getStringStateFlow(coroutineScope, key(key), default)
    override fun getStringOrNullStateFlow(key: String): StateFlow<String?> = settings.getStringOrNullStateFlow(coroutineScope, key(key))
    override fun getIntStateFlow(key: String, default: Int): StateFlow<Int> = settings.getIntStateFlow(coroutineScope,key(key),default)
    override fun getIntOrNullStateFlow(key: String): StateFlow<Int?> = settings.getIntOrNullStateFlow(coroutineScope,key(key))
    override fun getDoubleStateFlow(key: String, default: Double): StateFlow<Double> = settings.getDoubleStateFlow(coroutineScope,key(key), default)
    override fun getDoubleOrNullStateFlow(key: String): StateFlow<Double?> = settings.getDoubleOrNullStateFlow(coroutineScope,key(key))
    override fun getBooleanStateFlow(key: String, default: Boolean): StateFlow<Boolean> = settings.getBooleanStateFlow(coroutineScope,key(key), default)
    override fun getBooleanOrNullStateFlow(key: String): StateFlow<Boolean?> = settings.getBooleanOrNullStateFlow(coroutineScope,key(key))
    override fun getLongStateFlow(key: String, default: Long): StateFlow<Long> = settings.getLongStateFlow(coroutineScope,key(key), default)
    override fun getLongOrNullStateFlow(key: String): StateFlow<Long?> = settings.getLongOrNullStateFlow(coroutineScope,key(key))
}