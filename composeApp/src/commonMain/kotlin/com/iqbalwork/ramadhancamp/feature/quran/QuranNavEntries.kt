package com.iqbalwork.ramadhancamp.feature.quran

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranDetailScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranMainScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranSheetScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

@OptIn(ExperimentalMaterial3Api::class)
fun EntryProviderScope<NavKey>.quranTabEntries(nav: AppNavigationController) {
    entry<TabDestination.QuranMain>      { QuranMainScreen() }
    entry<TabDestination.QuranDetail>    { QuranDetailScreen() }
    entry<TabDestination.QuranSubDetail> { QuranSubDetailScreen() }
    entry<DialogDestination.QuranSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        QuranSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
