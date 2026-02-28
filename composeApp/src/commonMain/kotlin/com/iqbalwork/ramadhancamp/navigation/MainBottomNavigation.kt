package com.iqbalwork.ramadhancamp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

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

    NavigationBar {
        routes.forEach { route ->
            val isSelected = currentRoute == route
            NavigationBarItem(
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
