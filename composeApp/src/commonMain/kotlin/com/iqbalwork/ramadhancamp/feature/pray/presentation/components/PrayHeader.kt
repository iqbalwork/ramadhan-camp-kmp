package com.iqbalwork.ramadhancamp.feature.pray.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import kotlinx.datetime.LocalDate

@Composable
fun PrayHeader(
    city: String,
    selectedDate: LocalDate,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = city,
                style = typography.headlineSmall,
                color = colors.accentPrimary
            )
            Text(
                text = selectedDate.formatDisplay(),
                style = typography.labelSmall,
                color = colors.textSecondary
            )
        }
        IconButton(onClick = onCalendarClick) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "Pick date",
                tint = colors.textSecondary
            )
        }
    }
}

private fun LocalDate.formatDisplay(): String {
    val dayName = dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    val monthName = month.name.take(3).lowercase().replaceFirstChar { it.uppercase() }
    return "$dayName, $dayOfMonth $monthName $year"
}
