package com.iqbalwork.ramadhancamp.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun HomeMainHeader(
    modifier: Modifier = Modifier,
    greetingText: String,
    city: String,
    country: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier
                .heightIn(min = 28.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = greetingText,
                style = RamadhanTheme.typography.headlineLarge,
                color = RamadhanTheme.colors.textPrimary
            )
        }

        Row(
            modifier = Modifier
                .heightIn(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = RamadhanTheme.colors.bgSurface
            )
            Text(
                text = "$city, $country",
                style = RamadhanTheme.typography.bodyMedium,
                color = RamadhanTheme.colors.textPrimary
            )
        }
    }
}

@Preview()
@Composable
private fun HomeMainHeaderPreview() {
    RamadhanTheme {
        HomeMainHeader(
            greetingText = "Assalamu'alaikum, Iqbal",
            city = "Jakarta Selatan",
            country = "Indonesia",
            modifier = Modifier.fillMaxWidth().background(RamadhanTheme.colors.bgPrimary)
        )
    }
}
