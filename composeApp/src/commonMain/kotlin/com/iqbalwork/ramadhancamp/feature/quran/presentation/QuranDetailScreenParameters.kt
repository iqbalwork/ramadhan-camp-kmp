package com.iqbalwork.ramadhancamp.feature.quran.presentation

import com.iqbalwork.ramadhancamp.shared.common.ui.ScreenUiParams
import kotlinx.serialization.Serializable

@Serializable
data class QuranDetailScreenParameters(
    val surahId: Int,
    val scrollToAyat: Int? = null
) : ScreenUiParams()
