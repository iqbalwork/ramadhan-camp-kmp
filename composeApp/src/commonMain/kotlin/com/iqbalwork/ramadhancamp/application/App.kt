package com.iqbalwork.ramadhancamp.application

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import com.iqbalwork.ramadhancamp.navigation.BookmarkRoute
import com.iqbalwork.ramadhancamp.navigation.BottomNavRoute
import com.iqbalwork.ramadhancamp.navigation.HomeRoute
import com.iqbalwork.ramadhancamp.navigation.MainBottomNavigation
import com.iqbalwork.ramadhancamp.navigation.QiblaRoute
import com.iqbalwork.ramadhancamp.navigation.QuranRoute
import com.iqbalwork.ramadhancamp.navigation.bottomNavRoutes
import com.iqbalwork.ramadhancamp.presentation.bookmark.BookmarkScreen
import com.iqbalwork.ramadhancamp.presentation.home.HomeScreen
import com.iqbalwork.ramadhancamp.presentation.qibla.QiblaScreen
import com.iqbalwork.ramadhancamp.presentation.quran.QuranScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        val backStack: MutableList<BottomNavRoute> =
            rememberSerializable(serializer = SnapshotStateListSerializer()) {
                mutableStateListOf(HomeRoute)
            }

        Scaffold(
            bottomBar = {
                MainBottomNavigation(
                    backStack = backStack,
                    routes = bottomNavRoutes
                )
            }
        ) { paddingValues ->
            NavDisplay(
                modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
                backStack = backStack,
                onBack = {
                    if (backStack.size > 1) {
                        backStack.removeAt(backStack.size - 1)
                    }
                },
                entryProvider = entryProvider {
                    entry<HomeRoute> {
                        HomeScreen()
                    }
                    entry<QuranRoute> {
                        QuranScreen()
                    }
                    entry<QiblaRoute> {
                        QiblaScreen()
                    }
                    entry<BookmarkRoute> {
                        BookmarkScreen()
                    }
                }
            )
        }
    }
}
