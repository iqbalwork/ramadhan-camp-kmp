package com.iqbalwork.ramadhancamp.feature.quran.presentation.model

import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEffect
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEvent

data class QuranMainState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val surahs: List<Surah> = emptyList(),
    val searchQuery: String = ""
)

sealed interface QuranMainEvent : UiEvent {
    data class Search(val query: String) : QuranMainEvent
    data class SurahClicked(val surahId: Int) : QuranMainEvent
}

sealed interface QuranMainEffect : UiEffect
