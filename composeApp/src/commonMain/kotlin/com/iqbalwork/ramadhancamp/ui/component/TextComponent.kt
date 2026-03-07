package com.iqbalwork.ramadhancamp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.ui.theme.RamadhanTypography

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun LocationLabel(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.LocationOn,
    location: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White
        )
        Text(text = location)
    }
}

@Composable
fun TextSectionLabel(modifier: Modifier = Modifier, label: String) {
    Text(text = label, style = RamadhanTypography.titleMedium, modifier = modifier)
}
