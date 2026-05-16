package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.home.domain.model.LastSurahRead
import com.iqbalwork.ramadhancamp.feature.home.domain.usecase.UpdateLastSurahRead
import com.iqbalwork.ramadhancamp.feature.quran.domain.repository.QuranRepository
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranDetailState
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranSheetScreenParameters
import com.iqbalwork.ramadhancamp.shared.common.audio.AudioController
import com.iqbalwork.ramadhancamp.shared.common.navigation.AyatNumberResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResultData
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager
import com.iqbalwork.ramadhancamp.shared.common.ui.components.snackbar.RamadhanSnackBarProps
import com.iqbalwork.ramadhancamp.shared.common.ui.components.snackbar.SnackBarData
import com.iqbalwork.ramadhancamp.shared.common.ui.utils.TextResource
import com.iqbalwork.ramadhancamp.shared.common.utils.AppError
import com.iqbalwork.ramadhancamp.shared.common.utils.date.getCurrentDateLocalized
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.image_danger_error

class QuranDetailViewModel(
    params: QuranDetailScreenParameters,
    navigationManager: NavigationManager,
    private val quranRepository: QuranRepository,
    private val shareManager: ShareManager,
    private val updateLastSurahRead: UpdateLastSurahRead,
    private val audioController: AudioController,
) : BaseViewModel<QuranDetailScreenParameters, QuranDetailState, QuranDetailEvent, QuranDetailEffect>(
    params, QuranDetailState(), navigationManager,
    resultKeys = arrayOf("quran_sheet_play")
) {

    val mediaPlayerHost get() = audioController.mediaPlayerHost


    private var pendingSeekMs: Long? = null

    init {
        observeAudioState()
        loadSurahDetail()
    }

    private fun observeAudioState() {
        audioController.isPlaying.onEach { isPlaying ->
            updateState { copy(isPlaying = isPlaying) }
        }.launchIn(viewModelScope)

        audioController.currentTimeMs.onEach { ms ->
            if (ms != 0L || pendingSeekMs == null || state.value.isPlaying) {
                updateState { copy(currentTimeMs = ms) }
            }
        }.launchIn(viewModelScope)

        audioController.totalTimeMs.onEach { ms ->
            updateState { copy(totalTimeMs = ms) }
        }.launchIn(viewModelScope)

        audioController.isBuffering.onEach { isBuffering ->
            updateState { copy(isBuffering = isBuffering) }
        }.launchIn(viewModelScope)

        audioController.mediaEnded.onEach {
            val currentAyat = state.value.playingAyat
            val ayatList = state.value.surahDetail?.ayat
            if (currentAyat != null && ayatList != null) {
                val index = ayatList.indexOf(currentAyat)
                if (index != -1 && index < ayatList.size - 1) {
                    handleEvent(QuranDetailEvent.PlayAudio(ayatList[index + 1]))
                } else {
                    audioController.stop()
                    updateState { copy(isPlaying = false, currentTimeMs = 0L) }
                }
            } else {
                audioController.stop()
                updateState { copy(isPlaying = false, currentTimeMs = 0L) }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadSurahDetail() {
        updateState { copy(isLoading = true, appError = null) }
        viewModelScope.launch {
            quranRepository.getSurahDetail(params.surahId)
                .onSuccess { detail ->
                    updateState { copy(isLoading = false, surahDetail = detail) }
                }
                .onFailure {
                    updateState { copy(isLoading = false, appError = it as? AppError ?: AppError.UnexpectedError("Unknown error", it)) }
                }
        }
    }

    override fun handleEvent(event: QuranDetailEvent) {
        when (event) {
            is QuranDetailEvent.PlayAudio -> {
                pendingSeekMs = null
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(event.ayat)
                val nextUrl = if (index != -1 && index < ayatList.size - 1)
                    ayatList[index + 1].audioUrl else null
                
                updateState { 
                    copy(
                        playingAyat = event.ayat, 
                        nextAyatAudioUrl = nextUrl,
                        isPlaying = true,
                        isBuffering = true,
                        autoScrolledToPlayingAyat = false,
                        currentTimeMs = 0L,
                    ) 
                }

                val url = event.ayat.audioUrl
                if (url == audioController.lastLoadedUrl.value) {
                    audioController.seekToZero()
                    audioController.play()
                } else {
                    audioController.loadUrl(url)
                    viewModelScope.launch {
                        delay(100)
                        audioController.play()
                    }
                }

                val surahDetail = state.value.surahDetail ?: return
                viewModelScope.launch {
                    updateLastSurahRead(
                        LastSurahRead(
                            surahId = surahDetail.number,
                            surahName = surahDetail.namaLatin,
                            ayatNumber = event.ayat.nomorAyat,
                            readDate = getCurrentDateLocalized()
                        )
                    )
                }
            }
            is QuranDetailEvent.StopAudio -> {
                audioController.stop()
                updateState { copy(playingAyat = null) }
            }
            is QuranDetailEvent.TogglePlayPause -> {
                if (state.value.isPlaying) {
                    audioController.pause()
                } else {
                    val url = state.value.playingAyat?.audioUrl
                    if (url != null && url != audioController.lastLoadedUrl.value) {
                        audioController.loadUrl(url)
                        viewModelScope.launch {
                            delay(100)
                            pendingSeekMs?.let {
                                val seekPos = if (it >= state.value.totalTimeMs) 0f else it / 1000f
                                audioController.seekTo(seekPos.toLong() * 1000)
                                if (seekPos == 0f) updateState { copy(currentTimeMs = 0L) }
                                pendingSeekMs = null
                            }
                            audioController.play()
                        }
                    } else {
                        pendingSeekMs?.let {
                            val seekPos = if (it >= state.value.totalTimeMs) 0f else it / 1000f
                            audioController.seekTo(seekPos.toLong() * 1000)
                            if (seekPos == 0f) updateState { copy(currentTimeMs = 0L) }
                            pendingSeekMs = null
                        }
                        if (state.value.currentTimeMs >= state.value.totalTimeMs && state.value.totalTimeMs > 0L) {
                            val ayat = state.value.playingAyat
                            if (ayat != null) {
                                handleEvent(QuranDetailEvent.PlayAudio(ayat))
                            }
                        } else {
                            audioController.play()
                        }
                    }
                }
            }
            is QuranDetailEvent.PlayNextAyat -> {
                val currentAyat = state.value.playingAyat ?: return
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(currentAyat)
                if (index != -1 && index < ayatList.size - 1) {
                    val nextAyat = ayatList[index + 1]
                    handleEvent(QuranDetailEvent.PlayAudio(nextAyat))
                } else {
                    audioController.stop()
                    updateState { copy(playingAyat = null, nextAyatAudioUrl = null) }
                }
            }
            is QuranDetailEvent.PlayPrevAyat -> {
                val currentAyat = state.value.playingAyat ?: return
                val ayatList = state.value.surahDetail?.ayat ?: return
                val index = ayatList.indexOf(currentAyat)
                if (index != -1) {
                    if (state.value.currentTimeMs > 2000L || index == 0) {
                        audioController.seekToZero()
                        if (!state.value.isPlaying) {
                            audioController.play()
                        }
                    } else {
                        val prevAyat = ayatList[index - 1]
                        handleEvent(QuranDetailEvent.PlayAudio(prevAyat))
                    }
                }
            }
            is QuranDetailEvent.ShareAyat -> {
                shareManager.shareText(event.text)
            }
            is QuranDetailEvent.AyatRead -> {
                viewModelScope.launch {
                    updateLastSurahRead(
                        LastSurahRead(
                            surahId = event.surahId,
                            surahName = event.surahName,
                            ayatNumber = event.ayatNumber,
                            readDate = getCurrentDateLocalized()
                        )
                    )
                }
            }
            is QuranDetailEvent.Back -> {
                navigationManager.back()
            }
            is QuranDetailEvent.OpenAyatSheet -> {
                val params = QuranSheetScreenParameters(
                    surahId = state.value.surahDetail?.number ?: return,
                    ayatNumber = event.ayat.nomorAyat,
                    surahName = state.value.surahDetail?.namaLatin ?: "",
                    teksArab = event.ayat.teksArab,
                    teksLatin = event.ayat.teksLatin,
                    teksIndonesia = event.ayat.teksIndonesia,
                    audioUrl = event.ayat.audioUrl
                )
                navigationManager.showDialog(DialogDestination.QuranSheet(params))
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
            is QuranDetailEvent.Retry -> {
                loadSurahDetail()
            }
            is QuranDetailEvent.OnScreenDispose -> {
                pendingSeekMs = pendingSeekMs ?: state.value.currentTimeMs
                audioController.pause()
            }
            is QuranDetailEvent.OnScreenResume -> {
                // If playingAyat != null, we just wait for user to click play
            }
            is QuranDetailEvent.OnSeekAudio -> {
                audioController.seekTo(event.positionMs)
            }
            is QuranDetailEvent.AutoScrollToPlayingAyatConsumed -> {
                updateState { copy(autoScrolledToPlayingAyat = true) }
            }
            is QuranDetailEvent.InitialScrollConsumed -> {
                updateState { copy(hasScrolledToInitialAyah = true) }
            }
        }
    }

    override fun navigationResultSuccess(key: String, data: NavigationResultData?) {
        when (key) {
            "quran_sheet_play" -> {
                val ayatNumber = (data as? AyatNumberResult)?.ayatNumber ?: return
                val ayat = state.value.surahDetail?.ayat?.find { it.nomorAyat == ayatNumber } ?: return
                handleEvent(QuranDetailEvent.PlayAudio(ayat))
            }
            else -> super.navigationResultSuccess(key, data)
        }
    }
}


