package com.iqbalwork.ramadhancamp.feature.pray.data.datasource

import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.boolean
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers

private const val PRAY_PREF_SCOPE = "PRAY_PREF_SCOPE"

class PrayPreferences(prefs: AppPreferences) {
    private val scoped = prefs.scope(PRAY_PREF_SCOPE)

    var isFajrAlarmOn: Boolean by scoped.boolean("alarm_subuh", default = false)
    var isDzuhurAlarmOn: Boolean by scoped.boolean("alarm_dzuhur", default = false)
    var isAsrAlarmOn: Boolean by scoped.boolean("alarm_ashar", default = false)
    var isMaghribAlarmOn: Boolean by scoped.boolean("alarm_maghrib", default = false)
    var isIshaAlarmOn: Boolean by scoped.boolean("alarm_isya", default = false)

    fun getAlarmState(prayerKey: Prayers): Boolean = when (prayerKey) {
        Prayers.SUBUH   -> isFajrAlarmOn
        Prayers.DZUHUR  -> isDzuhurAlarmOn
        Prayers.ASHAR   -> isAsrAlarmOn
        Prayers.MAGHRIB -> isMaghribAlarmOn
        Prayers.ISYA    -> isIshaAlarmOn
    }

    fun setAlarmState(prayerKey: Prayers, enabled: Boolean) {
        when (prayerKey) {
            Prayers.SUBUH   -> isFajrAlarmOn = enabled
            Prayers.DZUHUR  -> isDzuhurAlarmOn = enabled
            Prayers.ASHAR   -> isAsrAlarmOn = enabled
            Prayers.MAGHRIB -> isMaghribAlarmOn = enabled
            Prayers.ISYA    -> isIshaAlarmOn = enabled
        }
    }


    fun getAllAlarmsState(): Map<Prayers, Boolean> = Prayers.entries.associateWith { getAlarmState(it) }
}
