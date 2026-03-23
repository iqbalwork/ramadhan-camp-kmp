package com.iqbalwork.ramadhancamp.feature.home.presentation.locationpicker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.iqbalwork.ramadhancamp.feature.home.presentation.locationpicker.components.LocationPickerContent
import com.iqbalwork.ramadhancamp.feature.home.presentation.locationpicker.model.LocationPickerEvent
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerScreen() {
    val viewModel: LocationPickerViewModel = rememberViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val action = viewModel.rememberDispatch()
    LocationPickerContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        action = action
    )

    NavigationBackHandler(
        isBackEnabled = state.selectedProvince != null,
        state = rememberNavigationEventState(NavigationEventInfo.None),
        onBackCompleted = { action(LocationPickerEvent.Cancel) }
    )
}
