package com.iqbalwork.ramadhancamp.feature.quran.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.feature.quran.domain.model.AyatSearchResult
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun AyatSearchResultItem(
    result: AyatSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Header: surah name + ayat number
        Text(
            text = "${result.surahName} • Ayat ${result.ayat.nomorAyat}",
            style = typography.labelSmall,
            color = colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        // Arabic text (right-aligned)
        Text(
            text = result.ayat.teksArab,
            style = typography.headlineLarge,
            color = colors.textPrimary,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )

        // Latin transliteration
        Text(
            text = result.ayat.teksLatin,
            style = typography.bodyLarge,
            color = colors.textSecondary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        // Indonesian translation
        Text(
            text = result.ayat.teksIndonesia,
            style = typography.bodyLarge,
            color = colors.textMuted,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}
