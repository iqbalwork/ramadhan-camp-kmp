package com.iqbalwork.ramadhancamp.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iqbalwork.ramadhancamp.ui.theme.DarkBackground
import com.iqbalwork.ramadhancamp.ui.theme.DarkSurface
import com.iqbalwork.ramadhancamp.ui.theme.PrimaryGreen
import com.iqbalwork.ramadhancamp.ui.theme.SecondaryGreen

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Composable
fun MainBottomNavigation(
    backStack: MutableList<BottomNavRoute>,
    routes: List<BottomNavRoute>
) {

    val currentRoute = backStack.lastOrNull()

    NavigationBar(
        containerColor = DarkSurface,
        windowInsets = WindowInsets(bottom = 4.dp)
    ) {
        routes.forEach { route ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = SecondaryGreen,
                    unselectedTextColor = SecondaryGreen,
                    indicatorColor = PrimaryGreen
                ),
                icon = { Icon(imageVector = route.icon, contentDescription = null) },
                label = { Text(text = route.name) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        // for bottom navigation, we usually want to replace the current top level route
                        // or clear backstack and add the new one
                        if (backStack.lastOrNull() is BottomNavRoute) {
                            backStack.removeAt(backStack.size - 1)
                        }
                        backStack.add(route)
                    }
                },
            )
        }
    }
}
