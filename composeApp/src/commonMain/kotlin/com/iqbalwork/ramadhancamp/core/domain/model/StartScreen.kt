package com.iqbalwork.ramadhancamp.core.domain.model

sealed interface StartScreen {
    data object Main : StartScreen
    data object Auth : StartScreen
}