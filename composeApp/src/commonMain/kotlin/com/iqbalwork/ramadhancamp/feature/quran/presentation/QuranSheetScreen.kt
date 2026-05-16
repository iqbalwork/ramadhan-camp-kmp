package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.bookmark.domain.model.Category
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranSheetState
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.SheetStep
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import org.koin.core.parameter.parametersOf

@Composable
fun QuranSheetScreen(params: QuranSheetScreenParameters) {
    val viewModel: QuranSheetViewModel = rememberViewModel { parametersOf(params) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val action = viewModel.rememberDispatch()

    // Handle effects
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is QuranSheetEffect.CopyToClipboard -> {
                    // Clipboard handling will be implemented in a follow-up
                }
                is QuranSheetEffect.DismissSheet -> {
                    action(QuranSheetEvent.Dismiss)
                }
            }
        }
    }

    // Snackbar for bookmark success
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.bookmarkMessage) {
        state.bookmarkMessage?.let {
            snackbarHostState.showSnackbar(it)
            action(QuranSheetEvent.BookmarkSuccessHandled)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                (slideInHorizontally { it / 4 } + fadeIn()) togetherWith
                    (slideOutHorizontally { -it / 4 } + fadeOut())
            },
            label = "step_transition"
        ) { currentStep ->
            when (currentStep) {
                SheetStep.MainActions -> MainActionsContent(state, action, params)
                SheetStep.PlaylistPicker -> PlaylistPickerContent(state, action)
                SheetStep.CreatePlaylist -> CreatePlaylistContent(state, action)
            }
        }
    }
}

@Composable
private fun MainActionsContent(
    state: QuranSheetState,
    action: (QuranSheetEvent) -> Unit,
    params: QuranSheetScreenParameters
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Pilihan Ayat",
            style = typography.headlineSmall,
            color = colors.textPrimary,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Surah context subtitle
        Text(
            text = "QS ${params.surahName}: ${params.ayatNumber}",
            style = typography.labelSmall,
            color = colors.textMuted,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Action buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionItem(
                icon = Icons.Default.PlayArrow,
                label = "Putar Audio",
                onClick = { action(QuranSheetEvent.PlayAudio) }
            )
            ActionItem(
                icon = Icons.Default.BookmarkBorder,
                label = "Simpan ke\nPlaylist",
                onClick = { action(QuranSheetEvent.Bookmark) }
            )
            ActionItem(
                icon = Icons.Default.Share,
                label = "Bagikan Ayat",
                onClick = { action(QuranSheetEvent.Share) }
            )
            ActionItem(
                icon = Icons.Default.ContentCopy,
                label = "Salin Ayat",
                onClick = { action(QuranSheetEvent.Copy) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Cancel button
        OutlinedButton(
            onClick = { action(QuranSheetEvent.Dismiss) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.textPrimary
            ),
            border = BorderStroke(1.dp, colors.divider)
        ) {
            Text(
                text = "Batal",
                style = typography.labelLarge,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PlaylistPickerContent(
    state: QuranSheetState,
    action: (QuranSheetEvent) -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(modifier = Modifier.fillMaxWidth()) {
        // Title row with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { action(QuranSheetEvent.BackFromStep) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = colors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Tambah ke Playlist",
                style = typography.headlineSmall,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { action(QuranSheetEvent.OnSearchQueryChanged(it)) },
            placeholder = {
                Text(
                    "Cari playlist...",
                    style = typography.bodyLarge,
                    color = colors.textMuted
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Search,
                    contentDescription = null,
                    tint = colors.textMuted,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accentPrimary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = colors.accentPrimary,
                focusedContainerColor = colors.bgSecondary,
                unfocusedContainerColor = colors.bgSecondary,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = typography.bodyLarge.copy(color = colors.textPrimary)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable list
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 360.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // "Buat playlist baru" row — always visible
            PlaylistCreateRow(
                onClick = { action(QuranSheetEvent.OpenCreatePlaylist) }
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (state.categories.isEmpty() && state.searchQuery.isNotBlank()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Playlist tidak ditemukan",
                        style = typography.bodyLarge,
                        color = colors.textMuted
                    )
                }
            } else {
                state.categories.forEach { category ->
                    PlaylistCategoryRow(
                        category = category,
                        onClick = { action(QuranSheetEvent.SelectCategory(category.id)) }
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun PlaylistCreateRow(onClick: () -> Unit) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // + icon circle with green translucent background
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    colors.accentPrimary.copy(alpha = 0.15f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = "Buat playlist baru",
                tint = colors.accentEmerald,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Buat playlist baru",
            style = typography.labelLarge,
            color = colors.textPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun PlaylistCategoryRow(
    category: Category,
    onClick: () -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Folder icon in rounded square
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(colors.bgSurface, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Folder,
                contentDescription = null,
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = category.name,
            style = typography.bodyLarge,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "Tambah ke playlist ini",
            tint = colors.textMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun CreatePlaylistContent(
    state: QuranSheetState,
    action: (QuranSheetEvent) -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    // Local state for playlist name since ViewModel has no dedicated event for it
    var playlistName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Title row with back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { action(QuranSheetEvent.BackFromStep) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = colors.textPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Playlist Baru",
                style = typography.headlineSmall,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Subtitle
        Text(
            text = "Buat Playlist Baru",
            style = typography.headlineSmall,
            color = colors.textPrimary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Name text field
        OutlinedTextField(
            value = playlistName,
            onValueChange = { playlistName = it },
            placeholder = {
                Text(
                    "Nama playlist...",
                    style = typography.bodyLarge,
                    color = colors.textMuted
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 48.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.accentPrimary,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = colors.accentEmerald,
                focusedContainerColor = colors.bgSecondary,
                unfocusedContainerColor = colors.bgSecondary,
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = typography.bodyLarge.copy(color = colors.textPrimary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Simpan button
        Button(
            onClick = { action(QuranSheetEvent.CreateNewPlaylist(playlistName)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accentPrimary,
                contentColor = colors.textOnLight,
                disabledContainerColor = colors.accentPrimary.copy(alpha = 0.4f),
                disabledContentColor = colors.textOnLight.copy(alpha = 0.6f)
            ),
            enabled = playlistName.isNotBlank() && !state.isSaving
        ) {
            if (state.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = colors.textOnLight,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Simpan",
                    style = typography.labelLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Batal button
        OutlinedButton(
            onClick = { action(QuranSheetEvent.BackFromStep) },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = colors.textPrimary
            ),
            border = BorderStroke(1.dp, colors.divider)
        ) {
            Text(
                text = "Batal",
                style = typography.labelLarge,
                color = colors.textPrimary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun ActionItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(colors.bgSecondary, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = colors.accentEmerald,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            style = typography.labelSmall,
            color = colors.textSecondary,
            textAlign = TextAlign.Center,
            minLines = 2
        )
    }
}
