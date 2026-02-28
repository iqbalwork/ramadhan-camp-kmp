package com.iqbalwork.ramadhancamp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
@Serializable
sealed interface Route

@Serializable
sealed interface BottomNavRoute : Route {
    val icon: ImageVector
    val name: String
}

@Serializable
data object HomeRoute : BottomNavRoute {
    override val icon: ImageVector
        get() = Icons.Default.Home
    override val name: String
        get() = "Home"
}

@Serializable
data object QuranRoute : BottomNavRoute {
    override val icon: ImageVector
        get() = Icons.AutoMirrored.Filled.MenuBook
    override val name: String
        get() = "Quran"
}

@Serializable
data object QiblaRoute : BottomNavRoute {
    override val icon: ImageVector
        get() = Icons.Default.Navigation
    override val name: String
        get() = "Qibla"
}

@Serializable
data object BookmarkRoute : BottomNavRoute {
    override val icon: ImageVector
        get() = Icons.Default.Bookmark
    override val name: String
        get() = "Bookmark"
}

val bottomNavRoutes: List<BottomNavRoute> = listOf(
    HomeRoute,
    QuranRoute,
    QiblaRoute,
    BookmarkRoute
)
