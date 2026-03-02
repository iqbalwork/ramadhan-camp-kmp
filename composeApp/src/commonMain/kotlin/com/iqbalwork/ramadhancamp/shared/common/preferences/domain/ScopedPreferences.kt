package com.iqbalwork.ramadhancamp.shared.common.preferences.domain

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
}