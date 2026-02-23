package com.iqbalwork.ramadhancamp.feature.qibla

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaDetailScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaMainScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaSheetScreen
import com.iqbalwork.ramadhancamp.feature.qibla.presentation.QiblaSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

fun EntryProviderBuilder<NavKey>.qiblaTabEntries(nav: AppNavigationController) {
    entry<TabDestination.QiblaMain>      { QiblaMainScreen() }
    entry<TabDestination.QiblaDetail>    { QiblaDetailScreen() }
    entry<TabDestination.QiblaSubDetail> { QiblaSubDetailScreen() }
    entry<DialogDestination.QiblaSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        QiblaSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
