package com.iqbalwork.ramadhancamp.feature.main.presentation.mapper

import com.iqbalwork.ramadhancamp.feature.main.presentation.model.AppTabInfo
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.ic_filled_book_tab
import ramadhancamp.composeapp.generated.resources.ic_filled_bookmark_tab
import ramadhancamp.composeapp.generated.resources.ic_filled_compass_tab
import ramadhancamp.composeapp.generated.resources.ic_filled_home_tab
import ramadhancamp.composeapp.generated.resources.ic_outlined_book_tab
import ramadhancamp.composeapp.generated.resources.ic_outlined_bookmark_tab
import ramadhancamp.composeapp.generated.resources.ic_outlined_compass_tab
import ramadhancamp.composeapp.generated.resources.ic_outlined_home_tab
import ramadhancamp.composeapp.generated.resources.ic_prayer_tab


fun AppTab.toAppTabInfo(selected: Boolean): AppTabInfo {
    return when (this) {
        AppTab.Home -> AppTabInfo(
            title = "Beranda",
            selected = selected,
            defaultIcon = Res.drawable.ic_outlined_home_tab,
            selectedIcon = Res.drawable.ic_filled_home_tab
        )
        AppTab.Pray -> AppTabInfo(
            title = "Doa",
            selected = selected,
            defaultIcon = Res.drawable.ic_prayer_tab,
            selectedIcon = Res.drawable.ic_prayer_tab,
        )
        AppTab.Quran -> AppTabInfo(
            title = "Quran",
            selected = selected,
            defaultIcon = Res.drawable.ic_outlined_book_tab,
            selectedIcon = Res.drawable.ic_filled_book_tab
        )
        AppTab.Qibla -> AppTabInfo(
            title = "Kiblat",
            selected = selected,
            defaultIcon = Res.drawable.ic_outlined_compass_tab,
            selectedIcon = Res.drawable.ic_filled_compass_tab
        )
        AppTab.Bookmark -> AppTabInfo(
            title = "Bookmark",
            selected = selected,
            defaultIcon = Res.drawable.ic_outlined_bookmark_tab,
            selectedIcon = Res.drawable.ic_filled_bookmark_tab
        )
    }
}
