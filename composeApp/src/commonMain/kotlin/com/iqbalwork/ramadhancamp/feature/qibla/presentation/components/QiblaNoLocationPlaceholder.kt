package com.iqbalwork.ramadhancamp.feature.qibla.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.shared.common.ui.components.button.RamadhanButton
import com.iqbalwork.ramadhancamp.shared.common.ui.components.button.RamadhanButtonProps
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme
import org.jetbrains.compose.resources.stringResource
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.error_location_permission_denied_message
import ramadhancamp.composeapp.generated.resources.error_location_permission_denied_title
import ramadhancamp.composeapp.generated.resources.open_app_settings

@Composable
fun QiblaNoLocationPlaceholder(
    onRequestPermission: () -> Unit,
    onGoToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = RamadhanTheme.colors
    val typography = RamadhanTheme.typography

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOff,
            contentDescription = null,
            tint = colors.accentSecondary,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = stringResource(Res.string.error_location_permission_denied_title),
            style = typography.headlineSmall,
            color = colors.textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.error_location_permission_denied_message),
            style = typography.bodyLarge,
            color = colors.textMuted,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        RamadhanButton(
            onClick = onGoToSettings,
            variant = RamadhanButtonProps.Variant.Primary,
            text = stringResource(Res.string.open_app_settings),
            size = RamadhanButtonProps.Size.Middle
        )
    }
}
