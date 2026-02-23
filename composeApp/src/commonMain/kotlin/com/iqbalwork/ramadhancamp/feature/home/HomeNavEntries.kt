package com.iqbalwork.ramadhancamp.feature.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeDetailScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeMainScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeSheetScreen
import com.iqbalwork.ramadhancamp.feature.home.presentation.HomeSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.homeTabEntries(nav: AppNavigationController) {
    entry<TabDestination.HomeMain>      { HomeMainScreen() }
    entry<TabDestination.HomeDetail>    { HomeDetailScreen() }
    entry<TabDestination.HomeSubDetail> { HomeSubDetailScreen() }
    entry<DialogDestination.HomeSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        HomeSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
