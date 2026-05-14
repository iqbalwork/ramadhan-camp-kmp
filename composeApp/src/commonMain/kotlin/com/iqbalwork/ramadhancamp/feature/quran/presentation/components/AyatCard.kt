package com.iqbalwork.ramadhancamp.feature.quran.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Ayat
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun AyatCard(
    ayat: Ayat,
    onOptionsClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isActive) Modifier.background(RamadhanTheme.colors.bgSurface)
                else Modifier
            )
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isActive) Color.Transparent else RamadhanTheme.colors.bgSecondary)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .border(1.dp, RamadhanTheme.colors.accentGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = ayat.nomorAyat.toString(),
                    style = RamadhanTheme.typography.labelSmall,
                    color = RamadhanTheme.colors.textPrimary
                )
            }
            IconButton(onClick = onOptionsClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = RamadhanTheme.colors.accentGold
                )
            }
        }
        
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = ayat.teksArab,
                style = RamadhanTheme.typography.headlineLarge,
                color = RamadhanTheme.colors.textPrimary,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = ayat.teksLatin,
                style = RamadhanTheme.typography.bodyLarge,
                color = RamadhanTheme.colors.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ayat.teksIndonesia,
                style = RamadhanTheme.typography.bodyLarge,
                color = RamadhanTheme.colors.textMuted
            )
        }
        HorizontalDivider(color = RamadhanTheme.colors.divider, thickness = 1.dp)
    }
}
