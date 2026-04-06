package com.iqbalwork.ramadhancamp.shared.common.preferences.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ScopedPreferences {
    fun getString(key: String, default: String = ""): String
    fun putString(key: String, value: String)
    fun getInt(key: String, default: Int = 0): Int
    fun putInt(key: String, value: Int)
    fun getDouble(key: String, default: Double = 0.0): Double
    fun putDouble(key: String, value: Double)
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getLong(key: String, default: Long = 0L): Long
    fun putLong(key: String, value: Long)
    fun remove(key: String)
    fun clear()

    fun getStringStateFlow(key: String, default: String = ""): StateFlow<String>
    fun getStringOrNullStateFlow(key: String): StateFlow<String?>
    fun getIntStateFlow(key: String, default: Int = 0): StateFlow<Int>
    fun getIntOrNullStateFlow(key: String): StateFlow<Int?>
    fun getDoubleStateFlow(key: String, default: Double = 0.0): StateFlow<Double>
    fun getDoubleOrNullStateFlow(key: String): StateFlow<Double?>
    fun getBooleanStateFlow(key: String, default: Boolean = false): StateFlow<Boolean>
    fun getBooleanOrNullStateFlow(key: String): StateFlow<Boolean?>
    fun getLongStateFlow(key: String, default: Long = 0L): StateFlow<Long>
    fun getLongOrNullStateFlow(key: String): StateFlow<Long?>
}