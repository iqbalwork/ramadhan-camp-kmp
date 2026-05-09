package com.iqbalwork.ramadhancamp.feature.quran.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ContentCopy
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyatOptionsSheet(
    ayat: Ayat,
    onDismissRequest: () -> Unit,
    onPlayAudio: () -> Unit,
    onBookmark: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = colors.bgPrimary,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 32.dp, height = 4.dp)
                    .background(colors.textMuted, RoundedCornerShape(2.dp))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pilihan Ayat",
                style = typography.headlineSmall,
                color = colors.textPrimary,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionItem(
                    icon = Icons.Default.PlayArrow,
                    label = "Putar Audio",
                    onClick = onPlayAudio
                )
                ActionItem(
                    icon = Icons.Default.BookmarkBorder,
                    label = "Simpan ke\nFavorit",
                    onClick = onBookmark
                )
                ActionItem(
                    icon = Icons.Default.Share,
                    label = "Bagikan Ayat",
                    onClick = onShare
                )
                ActionItem(
                    icon = Icons.Default.ContentCopy,
                    label = "Salin Ayat",
                    onClick = onCopy
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colors.textPrimary
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.divider)
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
                tint = colors.accentPrimary,
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
