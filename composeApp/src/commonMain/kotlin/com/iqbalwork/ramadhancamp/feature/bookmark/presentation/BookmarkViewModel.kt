package com.iqbalwork.ramadhancamp.feature.bookmark.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.repository.BookmarkRepository
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model.BookmarkEffect
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model.BookmarkEvent
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model.BookmarkState
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranDetailScreenParameters
import com.iqbalwork.ramadhancamp.feature.quran.presentation.route.QuranTab
import com.iqbalwork.ramadhancamp.shared.common.audio.AudioController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class BookmarkViewModel(
    navigationManager: NavigationManager,
    private val bookmarkRepository: BookmarkRepository,
    private val audioController: AudioController
) : BaseViewModel<Unit, BookmarkState, BookmarkEvent, BookmarkEffect>(
    Unit, BookmarkState(), navigationManager
) {
    private val searchQueryFlow = MutableStateFlow("")

    init {
        loadCategories()
        observeBookmarks()
        observeAudioState()
    }

    private fun observeAudioState() {
        audioController.isPlaying.onEach { isPlaying ->
            updateState { copy(isPlaying = isPlaying) }
        }.launchIn(viewModelScope)

        audioController.currentTimeMs.onEach { ms ->
            updateState { copy(currentTimeMs = ms) }
        }.launchIn(viewModelScope)

        audioController.totalTimeMs.onEach { ms ->
            updateState { copy(totalTimeMs = ms) }
        }.launchIn(viewModelScope)

        audioController.isBuffering.onEach { isBuffering ->
            updateState { copy(isBuffering = isBuffering) }
        }.launchIn(viewModelScope)

        audioController.mediaEnded.onEach {
            audioController.stop()
            updateState { copy(playingBookmark = null, isPlaying = false, currentTimeMs = 0L) }
        }.launchIn(viewModelScope)
    }

    private fun loadCategories() {
        bookmarkRepository.getAllCategories()
            .onEach { categories ->
                updateState { copy(categories = categories) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeBookmarks() {
        searchQueryFlow
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                bookmarkRepository.searchBookmarks(query)
            }
            .onEach { allBookmarks ->
                val selectedCategoryId = state.value.selectedCategoryId
                val filtered = if (selectedCategoryId != null) {
                    allBookmarks.filter { it.categoryId == selectedCategoryId }
                } else {
                    allBookmarks
                }
                updateState { copy(bookmarks = filtered) }
            }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: BookmarkEvent) {
        when (event) {
            is BookmarkEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
                searchQueryFlow.value = event.query
            }
            is BookmarkEvent.OnCategorySelected -> {
                updateState { copy(selectedCategoryId = event.categoryId) }
                searchQueryFlow.value = state.value.searchQuery
            }
            is BookmarkEvent.OnAddBookmarkClick -> {
                navigationManager.showDialog(DialogDestination.BookmarkCreateCategory)
            }
            is BookmarkEvent.OnDeleteCategoryClicked -> {
                updateState { copy(categoryToDelete = event.category) }
            }
            is BookmarkEvent.ConfirmDeleteCategory -> {
                val category = state.value.categoryToDelete ?: return
                viewModelScope.launch {
                    bookmarkRepository.deleteCategory(category.id)
                    // If the deleted category was selected, reset filter
                    if (state.value.selectedCategoryId == category.id) {
                        updateState { copy(selectedCategoryId = null) }
                    }
                    updateState { copy(categoryToDelete = null) }
                }
            }
            is BookmarkEvent.DismissDeleteCategory -> {
                updateState { copy(categoryToDelete = null) }
            }
            is BookmarkEvent.OnBookmarkClick -> {
                val bookmark = event.bookmark
                navigationManager.switchTab(QuranTab)
                navigationManager.navigateToInsideTab(
                    TabDestination.QuranDetail(
                        QuranDetailScreenParameters(
                            surahId = bookmark.surahId,
                            scrollToAyat = bookmark.ayatNumber
                        )
                    )
                )
            }
            is BookmarkEvent.OnPlayClick -> {
                val bookmark = event.bookmark
                if (state.value.playingBookmark?.id == bookmark.id) {
                    if (state.value.isPlaying) {
                        audioController.pause()
                    } else {
                        audioController.play()
                    }
                } else {
                    updateState {
                        copy(
                            playingBookmark = bookmark,
                            isPlaying = true,
                            isBuffering = true,
                            currentTimeMs = 0L,
                            totalTimeMs = 0L
                        )
                    }
                    val url = bookmark.audioUrl
                    if (url == audioController.lastLoadedUrl.value) {
                        audioController.seekToZero()
                        audioController.play()
                    } else {
                        audioController.loadUrl(url)
                        viewModelScope.launch {
                            kotlinx.coroutines.delay(100)
                            audioController.play()
                        }
                    }
                }
            }
            is BookmarkEvent.TogglePlayPause -> {
                if (state.value.isPlaying) {
                    audioController.pause()
                } else if (state.value.playingBookmark != null) {
                    audioController.play()
                }
            }
            is BookmarkEvent.OnSeekAudio -> {
                audioController.seekTo(event.positionMs)
            }
        }
    }
}
