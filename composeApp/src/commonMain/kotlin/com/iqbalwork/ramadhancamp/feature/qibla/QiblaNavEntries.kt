package com.iqbalwork.ramadhancamp.feature.qibla

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaDetailScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaMainScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaSheetScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.qiblaTabEntries(nav: AppNavigationController) {
    entry<TabDestination.QiblaMain>      { QiblaMainScreen() }
    entry<TabDestination.QiblaDetail>    { QiblaDetailScreen() }
    entry<TabDestination.QiblaSubDetail> { QiblaSubDetailScreen() }
    entry<DialogDestination.QiblaSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        QiblaSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
