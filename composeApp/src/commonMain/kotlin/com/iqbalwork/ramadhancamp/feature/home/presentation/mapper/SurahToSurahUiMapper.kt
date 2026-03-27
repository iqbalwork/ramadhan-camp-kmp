package com.iqbalwork.ramadhancamp.feature.home.presentation.mapper

import com.iqbalwork.ramadhancamp.feature.home.domain.model.Surah
import com.iqbalwork.ramadhancamp.feature.home.presentation.model.SurahUiModel

fun Surah.toSurahUi(): SurahUiModel {
    return SurahUiModel(
        number = number,
        arabicName = arabicName,
        latinName = latinName,
        numberOfAyahs = numberOfAyahs,
        descentPlace = descentPlace
    )
}