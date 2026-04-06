package com.iqbalwork.ramadhancamp.feature.pray.data.datasource

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.boolean

private const val PRAY_PREF_SCOPE = "PRAY_PREF_SCOPE"

class PrayPreferences(prefs: AppPreferences) {
    private val scoped = prefs.scope(PRAY_PREF_SCOPE)

    var isImsakAlarmOn: Boolean by scoped.boolean("alarm_imsak", default = false)
    var isFajrAlarmOn: Boolean by scoped.boolean("alarm_subuh", default = false)
    var isDzuhurAlarmOn: Boolean by scoped.boolean("alarm_dzuhur", default = false)
    var isAsrAlarmOn: Boolean by scoped.boolean("alarm_ashar", default = false)
    var isMaghribAlarmOn: Boolean by scoped.boolean("alarm_maghrib", default = false)
    var isIshaAlarmOn: Boolean by scoped.boolean("alarm_isya", default = false)

    fun getAlarmState(prayerKey: String): Boolean = when (prayerKey) {
        "imsak"   -> isImsakAlarmOn
        "subuh"   -> isFajrAlarmOn
        "dzuhur"  -> isDzuhurAlarmOn
        "ashar"   -> isAsrAlarmOn
        "maghrib" -> isMaghribAlarmOn
        "isya"    -> isIshaAlarmOn
        else      -> false
    }

    fun setAlarmState(prayerKey: String, enabled: Boolean) {
        when (prayerKey) {
            "imsak"   -> isImsakAlarmOn = enabled
            "subuh"   -> isFajrAlarmOn = enabled
            "dzuhur"  -> isDzuhurAlarmOn = enabled
            "ashar"   -> isAsrAlarmOn = enabled
            "maghrib" -> isMaghribAlarmOn = enabled
            "isya"    -> isIshaAlarmOn = enabled
        }
    }

    val allAlarmKeys = listOf("imsak", "subuh", "dzuhur", "ashar", "maghrib", "isya")
}
