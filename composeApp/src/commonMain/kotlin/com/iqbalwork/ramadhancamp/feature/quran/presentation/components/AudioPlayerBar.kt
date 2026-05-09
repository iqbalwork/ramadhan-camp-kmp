package com.iqbalwork.ramadhancamp.feature.quran.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun AudioPlayerBar(
    surahName: String,
    reciterName: String,
    isPlaying: Boolean,
    isBuffering: Boolean = false,
    elapsedMs: Long = 0L,
    totalDurationMs: Long = 0L,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.bgSecondary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isBuffering) "Now Playing � Buffering..." else "Now Playing",
                    style = typography.labelSmall,
                    color = if (isBuffering) colors.textMuted else colors.accentGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = surahName,
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary,
                    maxLines = 1
                )
                Text(
                    text = reciterName,
                    style = typography.labelSmall,
                    color = colors.textMuted,
                    maxLines = 1
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "Previous",
                    tint = colors.textPrimary,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onPrev() }
                )

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(colors.accentPrimary)
                        .clickable { onPlayPause() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = colors.textOnLight,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "Next",
                    tint = colors.textPrimary,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { onNext() }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDuration(elapsedMs),
                style = typography.labelSmall,
                color = colors.textMuted
            )
            Text(
                text = if (totalDurationMs > 0L) formatDuration(totalDurationMs) else "--:--",
                style = typography.labelSmall,
                color = colors.textMuted
            )
        }

        val sliderProgress = if (totalDurationMs > 0L) elapsedMs.toFloat() / totalDurationMs.toFloat() else 0f

        if (totalDurationMs > 0L) {
            Slider(
                value = sliderProgress,
                onValueChange = {},
                onValueChangeFinished = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = colors.accentGold,
                    activeTrackColor = colors.accentGold,
                    inactiveTrackColor = colors.divider
                )
            )
        } else {
            androidx.compose.material3.LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = colors.accentGold,
                trackColor = colors.divider
            )
        }
    }
}

private fun formatDuration(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}