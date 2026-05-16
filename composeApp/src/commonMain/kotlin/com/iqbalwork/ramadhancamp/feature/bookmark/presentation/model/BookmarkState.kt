package com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model

import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Bookmark
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category

data class BookmarkState(
    val searchQuery: String = "",
    val selectedCategoryId: Long? = null,
    val categories: List<Category> = emptyList(),
    val bookmarks: List<Bookmark> = emptyList(),
    val isLoading: Boolean = false,
    val playingBookmark: Bookmark? = null,
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentTimeMs: Long = 0L,
    val totalTimeMs: Long = 0L,
    val categoryToDelete: Category? = null
)
