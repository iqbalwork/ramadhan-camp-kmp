package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.home.presentation.components.HomeMainScreenContent
import com.iqbalwork.ramadhancamp.feature.home.presentation.locationpicker.model.LocationResult
import com.iqbalwork.ramadhancamp.feature.home.presentation.model.HomeEvent
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.ScreenUiParams
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.koin.core.parameter.parametersOf

@Serializable
data class HomeMainScreenParameters(
    val locationData: LocationResult? = null
) : ScreenUiParams()

@Composable
fun HomeMainScreen(
    parameters: HomeMainScreenParameters
) {
    val viewModel: HomeViewModel = rememberViewModel(parameters = { parametersOf(parameters) })
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dispatch = viewModel.rememberDispatch()

    val lifecycleOwner = LocalLifecycleOwner.current
    LifecycleEventEffect(
        Lifecycle.Event.ON_RESUME,
        lifecycleOwner
    )
    {
        if (!state.screenData.haveInitialized || state.pickedThroughPicker) return@LifecycleEventEffect
        dispatch(HomeEvent.LoadInitialData)
    }

    HomeMainScreenContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        action = dispatch
    )
}