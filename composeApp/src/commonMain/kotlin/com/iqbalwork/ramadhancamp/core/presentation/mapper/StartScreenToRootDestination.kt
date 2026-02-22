package com.iqbalwork.ramadhancamp.core.presentation.mapper

import com.iqbalwork.ramadhancamp.core.domain.model.StartScreen
import com.iqbalwork.ramadhancamp.shared.common.navigation.RootDestination

fun StartScreen.toRootDestination(): RootDestination {
    return when (this) {
        StartScreen.Auth -> RootDestination.Auth
        StartScreen.Main -> RootDestination.Main()
    }
}