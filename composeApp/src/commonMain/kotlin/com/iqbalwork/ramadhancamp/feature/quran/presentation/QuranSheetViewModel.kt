package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Bookmark
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.repository.BookmarkRepository
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetState
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.SheetStep
import com.iqbalwork.ramadhancamp.shared.common.navigation.AyatNumberResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import com.iqbalwork.ramadhancamp.shared.common.utils.ShareManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class QuranSheetViewModel(
    params: QuranSheetScreenParameters,
    navigationManager: NavigationManager,
    private val shareManager: ShareManager,
    private val bookmarkRepository: BookmarkRepository,
) : BaseViewModel<QuranSheetScreenParameters, QuranSheetState, QuranSheetEvent, QuranSheetEffect>(
    params, QuranSheetState(), navigationManager
) {
    private val searchQueryFlow = MutableStateFlow("")

    init {
        combine(
            bookmarkRepository.getAllCategories(),
            searchQueryFlow
        ) { allCategories, query ->
            if (query.isBlank()) allCategories
            else allCategories.filter { it.name.contains(query, ignoreCase = true) }
        }.onEach { filtered ->
            updateState { copy(categories = filtered) }
        }.launchIn(viewModelScope)
    }

    override fun handleEvent(event: QuranSheetEvent) {
        when (event) {
            is QuranSheetEvent.PlayAudio -> {
                navigationManager.back(
                    NavigationResult.Success(
                        key = "quran_sheet_play",
                        value = AyatNumberResult(params.ayatNumber)
                    )
                )
            }
            is QuranSheetEvent.Bookmark -> {
                updateState { copy(step = SheetStep.PlaylistPicker) }
            }
            is QuranSheetEvent.Share -> {
                val shareText = buildString {
                    append(params.teksArab)
                    append("\n\n")
                    append(params.teksLatin)
                    append("\n\n")
                    append(params.teksIndonesia)
                }
                shareManager.shareText(shareText)
            }
            is QuranSheetEvent.Copy -> {
                val copyText = buildString {
                    append(params.teksArab)
                    append("\n\n")
                    append(params.teksLatin)
                    append("\n\n")
                    append(params.teksIndonesia)
                }
                sendEffect(QuranSheetEffect.CopyToClipboard(copyText))
            }
            is QuranSheetEvent.Dismiss -> {
                navigationManager.back()
            }
            is QuranSheetEvent.BookmarkSuccessHandled -> {
                updateState { copy(bookmarkMessage = null) }
            }
            is QuranSheetEvent.OpenPlaylistPicker -> {
                updateState { copy(step = SheetStep.PlaylistPicker) }
            }
            is QuranSheetEvent.OpenCreatePlaylist -> {
                updateState { copy(step = SheetStep.CreatePlaylist, newCategoryName = "") }
            }
            is QuranSheetEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
                searchQueryFlow.value = event.query
            }
            is QuranSheetEvent.SelectCategory -> {
                val categoryId = event.categoryId
                updateState { copy(isSaving = true) }
                viewModelScope.launch {
                    val bookmark = Bookmark(
                        id = 0,
                        ayahDetails = "${params.surahName}: ${params.ayatNumber}",
                        categoryId = categoryId,
                        timestamp = Clock.System.now().toEpochMilliseconds(),
                        surahId = params.surahId,
                        ayatNumber = params.ayatNumber,
                        surahName = params.surahName,
                        audioUrl = params.audioUrl, teksArab = params.teksArab, teksIndonesia = params.teksIndonesia
                    )
                    bookmarkRepository.addBookmark(bookmark)
                    updateState { copy(isSaving = false) }
                    sendEffect(QuranSheetEffect.DismissSheet)
                }
            }
            is QuranSheetEvent.CreateNewPlaylist -> {
                val name = event.name.trim()
                if (name.isEmpty()) return
                updateState { copy(isSaving = true) }
                viewModelScope.launch {
                    val newCategoryId = bookmarkRepository.addCategory(
                        Category(id = 0, name = name)
                    )
                    val bookmark = Bookmark(
                        id = 0,
                        ayahDetails = "${params.surahName}: ${params.ayatNumber}",
                        categoryId = newCategoryId,
                        timestamp = Clock.System.now().toEpochMilliseconds(),
                        surahId = params.surahId,
                        ayatNumber = params.ayatNumber,
                        surahName = params.surahName,
                        audioUrl = params.audioUrl, teksArab = params.teksArab, teksIndonesia = params.teksIndonesia
                    )
                    bookmarkRepository.addBookmark(bookmark)
                    updateState { copy(isSaving = false) }
                    sendEffect(QuranSheetEffect.DismissSheet)
                }
            }
            is QuranSheetEvent.BackFromStep -> {
                val currentStep = state.value.step
                when (currentStep) {
                    is SheetStep.MainActions -> {
                        navigationManager.back()
                    }
                    is SheetStep.PlaylistPicker -> {
                        updateState { copy(step = SheetStep.MainActions) }
                    }
                    is SheetStep.CreatePlaylist -> {
                        updateState { copy(step = SheetStep.PlaylistPicker) }
                    }
                }
            }
        }
    }
}
