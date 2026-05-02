package com.iqbalwork.ramadhancamp.shared.common.navigation

import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranDetailScreenParameters

object DeepLinkParser {
    fun parse(uri: String): TabDestination? {
        val regex = Regex("ramadhancamp://quran/(\\d+)(?:/(\\d+))?")
        val match = regex.find(uri)
        if (match != null) {
            val surah = match.groupValues.getOrNull(1)?.toIntOrNull() ?: return null
            val ayat = match.groupValues.getOrNull(2)?.takeIf { it.isNotEmpty() }?.toIntOrNull()
            return TabDestination.QuranDetail(QuranDetailScreenParameters(surahId = surah, scrollToAyat = ayat))
        }
        return null
    }
}
