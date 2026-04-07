package com.iqbalwork.ramadhancamp.feature.pray.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iqbalwork.ramadhancamp.feature.pray.presentation.model.PrayCountdownUiModel
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun NextPrayerCard(
    countdown: PrayCountdownUiModel,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(colors.bgSurface, colors.bgSecondary)
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "NEXT PRAYER",
                style = typography.labelSmall,
                color = colors.accentPrimary.copy(alpha = 0.8f),
                letterSpacing = 0.7.sp
            )

            Text(
                text = countdown.prayerName,
                style = typography.headlineLarge,
                color = colors.textPrimary,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "in ",
                    style = typography.bodyLarge,
                    color = colors.textSecondary
                )
                Text(
                    text = countdown.remainingTime,
                    style = typography.headlineLarge.copy(fontSize = 48.sp),
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = countdown.prevPrayerName,
                        style = typography.labelSmall,
                        color = colors.textSecondary
                    )
                    Text(
                        text = countdown.prevPrayerTime,
                        style = typography.bodyLarge,
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(32.dp),
                    color = colors.divider
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = countdown.nextPrayerName,
                        style = typography.labelSmall,
                        color = colors.textSecondary
                    )
                    Text(
                        text = countdown.nextPrayerTime,
                        style = typography.bodyLarge,
                        color = colors.textPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
