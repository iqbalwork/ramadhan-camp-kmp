package com.iqbalwork.ramadhancamp.shared.common.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.ViewModel
import com.iqbalwork.ramadhancamp.shared.common.navigation.LocalAppNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.emptyParametersHolder
import org.koin.core.qualifier.Qualifier

@Composable
internal inline fun <reified T : ViewModel> rememberViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val navAppController = LocalAppNavController.current
    val viewModel =
        koinViewModel<T>(
            qualifier = qualifier,
            parameters = {
                (parameters?.invoke() ?: emptyParametersHolder()).apply {
                    add(navAppController)
                }
            },
        )

    return viewModel
}