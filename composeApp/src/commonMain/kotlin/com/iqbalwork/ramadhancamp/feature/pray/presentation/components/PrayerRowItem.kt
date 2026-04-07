package com.iqbalwork.ramadhancamp.feature.pray.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.feature.pray.domain.model.Prayers
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayItemUiModel
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun PrayerRowItem(
    item: PrayItemUiModel,
    onAlarmToggle: (key: Prayers, enabled: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    val isHighlighted = item.isNextPrayer
    val rowAlpha = 1f

    val containerColor = if (isHighlighted)
        colors.bgSurface.copy(alpha = 0.2f)
    else
        colors.bgSecondary

    val borderModifier = if (isHighlighted)
        Modifier.border(
            width = 2.dp,
            color = colors.accentPrimary.copy(alpha = 0.3f),
            shape = RoundedCornerShape(12.dp)
        )
    else Modifier

    Box(
        modifier = modifier
            .fillMaxWidth()
            .alpha(rowAlpha)
            .clip(RoundedCornerShape(12.dp))
            .then(borderModifier)
            .background(containerColor)
    ) {
        if (isHighlighted) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = colors.accentPrimary,
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = if (isHighlighted) 20.dp else 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.displayName,
                    tint = if (isHighlighted) colors.accentPrimary else colors.textSecondary,
                    modifier = Modifier.size(22.dp)
                )
                Column {
                    Text(
                        text = item.displayName,
                        style = typography.bodyLarge,
                        color = if (isHighlighted) colors.accentPrimary else colors.textPrimary,
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold
                    )
                    if (isHighlighted) {
                        Text(
                            text = "NEXT PRAYER",
                            style = typography.labelSmall,
                            color = colors.textMuted
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.time,
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = colors.textPrimary
                )
                    IconButton(
                        onClick = { onAlarmToggle(item.key, !item.isAlarmOn) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (item.isAlarmOn)
                                Icons.Default.NotificationsActive
                            else
                                Icons.Default.NotificationsOff,
                            contentDescription = if (item.isAlarmOn) "Alarm on" else "Alarm off",
                            tint = if (item.isAlarmOn) colors.accentPrimary else colors.textMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }

            }
        }
    }
}
