package com.iqbalwork.ramadhancamp.feature.quran.presentation.model

import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEffect
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEvent

sealed interface SheetStep {
    data object MainActions : SheetStep
    data object PlaylistPicker : SheetStep
    data object CreatePlaylist : SheetStep
    data object RemoveFromPlaylist : SheetStep
}

data class QuranSheetState(
    val step: SheetStep = SheetStep.MainActions,
    val searchQuery: String = "",
    val categories: List<Category> = emptyList(),
    val newCategoryName: String = "",
    val isSaving: Boolean = false,
    val bookmarkCategories: List<Category> = emptyList(),
    val isBookmarked: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false,
    val bookmarkToDelete: Long? = null
)

sealed interface QuranSheetEvent : UiEvent {
    data object PlayAudio : QuranSheetEvent
    data object Bookmark : QuranSheetEvent
    data object Share : QuranSheetEvent
    data object Copy : QuranSheetEvent
    data object Dismiss : QuranSheetEvent
    data object OpenPlaylistPicker : QuranSheetEvent
    data object OpenCreatePlaylist : QuranSheetEvent
    data class OnSearchQueryChanged(val query: String) : QuranSheetEvent
    data class SelectCategory(val categoryId: Long) : QuranSheetEvent
    data class CreateNewPlaylist(val name: String) : QuranSheetEvent
    data object BackFromStep : QuranSheetEvent
    data class MarkForDeletion(val categoryId: Long) : QuranSheetEvent
    data object ConfirmDelete : QuranSheetEvent
    data object CancelDelete : QuranSheetEvent
    data object OpenDeletePlaylist : QuranSheetEvent
}

sealed interface QuranSheetEffect : UiEffect {
    data class CopyToClipboard(val text: String) : QuranSheetEffect
    data object DismissSheet : QuranSheetEffect
}
