package com.iqbalwork.ramadhancamp.feature.bookmark.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.components.BookmarkCard
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.components.CategoryChip
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model.BookmarkEvent
import com.iqbalwork.ramadhancamp.feature.bookmark.presentation.model.BookmarkState
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.AudioPlayerBar
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.ScreenContent
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun BookmarkScreen() {
    val viewModel: BookmarkViewModel = rememberViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dispatch = viewModel.rememberDispatch()

    ScreenContent(viewModel) {
        BookmarkContent(state = state, action = dispatch)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookmarkContent(
    state: BookmarkState,
    action: (BookmarkEvent) -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    // Delete category confirmation dialog
    val categoryToDelete = state.categoryToDelete
    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { action(BookmarkEvent.DismissDeleteCategory) },
            containerColor = colors.bgSecondary,
            titleContentColor = colors.textPrimary,
            textContentColor = colors.textSecondary,
            title = {
                Text(
                    text = "Delete Category?",
                    style = typography.headlineSmall,
                    color = colors.textPrimary
                )
            },
            text = {
                Text(
                    text = "All saved bookmarks inside \"${categoryToDelete.name}\" will be deleted.",
                    style = typography.bodyLarge,
                    color = colors.textSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { action(BookmarkEvent.ConfirmDeleteCategory) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accentPrimary,
                        contentColor = colors.textOnLight
                    )
                ) {
                    Text("Delete", style = typography.labelLarge)
                }
            },
            dismissButton = {
                TextButton(onClick = { action(BookmarkEvent.DismissDeleteCategory) }) {
                    Text("Cancel", color = colors.textMuted, style = typography.labelLarge)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks", style = typography.headlineSmall, color = colors.textPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.bgPrimary
                ),
                actions = {
                    IconButton(onClick = { action(BookmarkEvent.OnAddBookmarkClick) }) {
                        Text("+", color = colors.accentPrimary, style = typography.headlineLarge)
                    }
                }
            )
        },
        containerColor = colors.bgPrimary
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { action(BookmarkEvent.OnSearchQueryChanged(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Search bookmarks...", color = colors.textMuted) },
                    textStyle = typography.bodyLarge.copy(color = colors.textPrimary),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.accentPrimary,
                        unfocusedBorderColor = colors.divider,
                        cursorColor = colors.accentPrimary
                    ),
                    singleLine = true
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        CategoryChip(
                            name = "All",
                            isSelected = state.selectedCategoryId == null,
                            onClick = { action(BookmarkEvent.OnCategorySelected(null)) }
                        )
                    }
                    items(state.categories) { category ->
                        Box {
                            CategoryChip(
                                name = category.name,
                                isSelected = state.selectedCategoryId == category.id,
                                onClick = { action(BookmarkEvent.OnCategorySelected(category.id)) },
                                onLongClick = { action(BookmarkEvent.OnDeleteCategoryClicked(category)) }
                            )
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 0.dp,
                        bottom = if (state.playingBookmark != null) 200.dp else 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.bookmarks) { bookmark ->
                        BookmarkCard(
                            bookmark = bookmark,
                            categoryName = state.categories.find { it.id == bookmark.categoryId }?.name ?: "Unknown",
                            isActive = state.playingBookmark?.id == bookmark.id,
                            isPlaying = state.isPlaying,
                            onClick = { action(BookmarkEvent.OnBookmarkClick(bookmark)) },
                            onPlayClick = { action(BookmarkEvent.OnPlayClick(bookmark)) }
                        )
                    }
                }
            }

            if (state.playingBookmark != null) {
                val playingBookmark = state.playingBookmark
                AudioPlayerBar(
                    surahName = playingBookmark.surahName,
                    reciterName = "Misyari Rasyid",
                    currentTimeMs = state.currentTimeMs,
                    totalDurationMs = state.totalTimeMs,
                    isPlaying = state.isPlaying,
                    isBuffering = state.isBuffering,
                    onSeek = { action(BookmarkEvent.OnSeekAudio(it)) },
                    onPlayPause = { action(BookmarkEvent.TogglePlayPause) },
                    onNext = null,
                    onPrev = null,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }
}
