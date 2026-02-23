package com.iqbalwork.ramadhancamp.feature.quran

import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranDetailScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranMainScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranSheetScreen
import com.iqbalwork.ramadhancamp.feature.quran.presentation.QuranSubDetailScreen
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination

fun EntryProviderBuilder<NavKey>.quranTabEntries(nav: AppNavigationController) {
    entry<TabDestination.QuranMain>      { QuranMainScreen() }
    entry<TabDestination.QuranDetail>    { QuranDetailScreen() }
    entry<TabDestination.QuranSubDetail> { QuranSubDetailScreen() }
    entry<DialogDestination.QuranSheet>(metadata = BottomSheetSceneStrategy.bottomSheet()) {
        QuranSheetScreen(onDismiss = { nav.hideDialog() })
    }
}
