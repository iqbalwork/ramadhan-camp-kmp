package com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model

import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Bookmark
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category
import com.iqbalwork.ramadhancamp.shared.common.ui.UiEvent

sealed interface BookmarkEvent : UiEvent {
    data class OnSearchQueryChanged(val query: String) : BookmarkEvent
    data class OnCategorySelected(val categoryId: Long?) : BookmarkEvent
    data object OnAddBookmarkClick : BookmarkEvent
    data class OnBookmarkClick(val bookmark: Bookmark) : BookmarkEvent
    data class OnPlayClick(val bookmark: Bookmark) : BookmarkEvent
    data object TogglePlayPause : BookmarkEvent
    data class OnSeekAudio(val positionMs: Long) : BookmarkEvent
    data class OnDeleteCategoryClicked(val category: Category) : BookmarkEvent
    data object ConfirmDeleteCategory : BookmarkEvent
    data object DismissDeleteCategory : BookmarkEvent
}
