package com.iqbalwork.ramadhancamp.feature.home.data.datasource

import com.iqbalwork.ramadhancamp.feature.home.data.model.shalatSchedule.ShalatScheduleDto
import com.iqbalwork.ramadhancamp.shared.common.preferences.domain.AppPreferences
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.StateFlowPref
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.double
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.nullableIntStateFlowPref
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.nullableString
import com.iqbalwork.ramadhancamp.shared.common.preferences.utils.nullableStringStateFlowPref

private const val HOME_PREF_SCOPE = "HOME_PREF_SCOPE"

class HomePreferences(prefs: AppPreferences) {
    private val scoped = prefs.scope(HOME_PREF_SCOPE)

    var lastLatitude: Double by scoped.double("lat")
    var lastLongitude: Double by scoped.double("lon")
    var lastCity: String? by scoped.nullableString("lastCity")
    var lastProvince: String? by scoped.nullableString("lastProvince")
    var lastCountry: String? by scoped.nullableString("lastCountry")

    val lastCityStateFlow: StateFlowPref<String?> = scoped.nullableStringStateFlowPref("lastCity")
    val surahNameStateFlow: StateFlowPref<String?> = scoped.nullableStringStateFlowPref("surahName")
    val lastAyatNumberStateFlow: StateFlowPref<Int?> = scoped.nullableIntStateFlowPref("lastAyatNumber")
    val lastDateReadStateFlow: StateFlowPref<String?> = scoped.nullableStringStateFlowPref("lastDateRead")

    fun clear() = scoped.clear()
}