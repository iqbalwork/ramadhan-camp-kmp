package com.iqbalwork.ramadhancamp.feature.main.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.DrawableResource

data class AppTabInfo(
    val title: String,
    val selected: Boolean,
    private val defaultIcon: DrawableResource,
    private val selectedIcon: DrawableResource,
) {
    val icon = if (selected) selectedIcon else defaultIcon
}