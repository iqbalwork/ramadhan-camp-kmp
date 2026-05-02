package com.iqbalwork.ramadhancamp.feature.quran.presentation.model

import com.iqbalwork.ramadhancamp.feature.quran.domain.model.SurahDetail
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEffect
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEvent

data class QuranDetailState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val surahDetail: SurahDetail? = null,
    val currentlyPlayingAudioUrl: String? = null
)

sealed interface QuranDetailEvent : UiEvent {
    data class PlayAudio(val url: String) : QuranDetailEvent
    data object StopAudio : QuranDetailEvent
    data class ShareAyat(val text: String) : QuranDetailEvent
    data class AyatRead(val surahId: Int, val surahName: String, val ayatNumber: Int) : QuranDetailEvent
    data object Back : QuranDetailEvent
}

sealed interface QuranDetailEffect : UiEffect
