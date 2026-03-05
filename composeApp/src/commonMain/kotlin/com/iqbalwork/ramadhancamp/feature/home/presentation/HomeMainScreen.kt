package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.home.presentation.components.HomeMainScreenContent
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch

@Composable
fun HomeMainScreen() {
    val viewModel: HomeViewModel = rememberViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dispatch = viewModel.rememberDispatch()

    HomeMainScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        action = dispatch
    )

}
