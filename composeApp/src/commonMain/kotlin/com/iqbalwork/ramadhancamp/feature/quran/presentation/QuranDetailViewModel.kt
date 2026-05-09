package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.home.domain.model.LastSurahRead
import com.iqbalwork.ramadhancamp.feature.home.domain.usecase.UpdateLastSurahRead
import com.iqbalwork.ramadhancamp.feature.quran.domain.repository.QuranRepository
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager
import kotlinx.coroutines.launch
import com.iqbalwork.ramadhancamp.shared.common.ui.components.snackbar.RamadhanSnackBarProps
import com.iqbalwork.ramadhancamp.shared.common.ui.components.snackbar.SnackBarData
import com.iqbalwork.ramadhancamp.shared.common.ui.utils.TextResource
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.image_danger_error

class QuranDetailViewModel(
    params: QuranDetailScreenParameters,
    navigationManager: NavigationManager,
    private val quranRepository: QuranRepository,
    private val shareManager: ShareManager,
    private val updateLastSurahRead: UpdateLastSurahRead
) : BaseViewModel<QuranDetailScreenParameters, QuranDetailState, QuranDetailEvent, QuranDetailEffect>(
    params, QuranDetailState(), navigationManager
) {

    init {
        loadSurahDetail()
    }

    private fun loadSurahDetail() {
        updateState { copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            quranRepository.getSurahDetail(params.surahId)
                .onSuccess { detail ->
                    updateState { copy(isLoading = false, surahDetail = detail) }
                }
                .onFailure {
                    updateState { copy(isLoading = false, isError = true) }
                }
        }
    }

    override fun handleEvent(event: QuranDetailEvent) {
        when (event) {
            is QuranDetailEvent.PlayAudio -> {
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(event.ayat)
                val nextUrl = if (index != -1 && index < ayatList.size - 1)
                    ayatList[index + 1].audioUrl else null
                updateState { copy(playingAyat = event.ayat, nextAyatAudioUrl = nextUrl) }
            }
            is QuranDetailEvent.StopAudio -> {
                updateState { copy(playingAyat = null) }
            }
            is QuranDetailEvent.PlayNextAyat -> {
                val currentAyat = state.value.playingAyat ?: return
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(currentAyat)
                if (index != -1 && index < ayatList.size - 1) {
                    val nextAyat = ayatList[index + 1]
                    // Compute next-next URL for pre-buffering
                    val nextNextUrl = if (index + 1 < ayatList.size - 1)
                        ayatList[index + 2].audioUrl else null
                    updateState { copy(playingAyat = nextAyat, nextAyatAudioUrl = nextNextUrl) }
                } else {
                    updateState { copy(playingAyat = null, nextAyatAudioUrl = null) }
                }
            }
            is QuranDetailEvent.PlayPrevAyat -> {
                val currentAyat = state.value.playingAyat ?: return
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(currentAyat)
                if (index > 0) {
                    val prevAyat = ayatList[index - 1]
                    // Compute next URL relative to prev (for pre-buffering after going back)
                    val nextUrl = if (index < ayatList.size - 1)
                        ayatList[index].audioUrl else null  // current ayat = "next" relative to prev
                    updateState { copy(playingAyat = prevAyat, nextAyatAudioUrl = nextUrl) }
                }
            }
            is QuranDetailEvent.ShareAyat -> {
                shareManager.shareText(event.text)
            }
            is QuranDetailEvent.AyatRead -> {
                viewModelScope.launch {
                    updateLastSurahRead(
                        LastSurahRead(
                            surahName = event.surahName,
                            ayatNumber = event.ayatNumber,
                            readDate = kotlinx.datetime.Clock.System.now().toString()
                        )
                    )
                }
            }
            is QuranDetailEvent.Back -> {
                navigationManager.back()
            }
            is QuranDetailEvent.OnAyatClicked -> {
                updateState { copy(selectedAyatForOptions = event.ayat) }
            }
            is QuranDetailEvent.OnCloseOptionsSheet -> {
                updateState { copy(selectedAyatForOptions = null) }
            }
            is QuranDetailEvent.OnBookmarkClicked -> {
                updateState { copy(selectedAyatForOptions = null) }
            }
            is QuranDetailEvent.OnShareClicked -> {
                val shareText = "" + event.ayat.teksArab + "\n\n" + event.ayat.teksLatin + "\n\n" + event.ayat.teksIndonesia
                shareManager.shareText(shareText)
                updateState { copy(selectedAyatForOptions = null) }
            }
            is QuranDetailEvent.OnCopyClicked -> {
                updateState { copy(selectedAyatForOptions = null) }
            }
            is QuranDetailEvent.AudioError -> {
                showSnackBar(
                    SnackBarData(
                        message = TextResource.PlainText(event.message),
                        icon = Res.drawable.image_danger_error,
                        durationMillis = RamadhanSnackBarProps.Duration.Long,
                        position = RamadhanSnackBarProps.Position.Bottom
                    )
                )
            }
        }
    }
}
