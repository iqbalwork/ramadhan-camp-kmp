package com.iqbalwork.ramadhancamp.feature.quran.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.Surah
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun SurahListItem(
    surah: Surah,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentGold = RamadhanTheme.colors.accentGold
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .drawBehind {
                    drawCircle(color = accentGold, style = Stroke(width = 2.dp.toPx()))
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = surah.number.toString(),
                style = RamadhanTheme.typography.labelLarge,
                color = RamadhanTheme.colors.textPrimary
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = surah.namaLatin,
                style = RamadhanTheme.typography.headlineSmall,
                color = RamadhanTheme.colors.textPrimary
            )
            Text(
                text = "${surah.arti} • ${surah.jumlahAyat} Ayat",
                style = RamadhanTheme.typography.labelSmall,
                color = RamadhanTheme.colors.textSecondary
            )
        }
        
        Text(
            text = surah.nama,
            style = RamadhanTheme.typography.headlineLarge,
            color = accentGold,
            textAlign = TextAlign.End
        )
    }
}
