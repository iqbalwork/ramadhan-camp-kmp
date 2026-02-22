package com.iqbalwork.ramadhancamp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.iqbalwork.ramadhancamp.core.domain.model.StartScreen
import com.iqbalwork.ramadhancamp.core.presentation.mapper.toRootDestination
import com.iqbalwork.ramadhancamp.shared.common.bottomSheet.BottomSheetSceneStrategy
import com.iqbalwork.ramadhancamp.shared.common.navigation.ResultNavigationRepository
import com.iqbalwork.ramadhancamp.shared.common.navigation.RootDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.rememberAppNavigationController
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject

import ramadhancamp.composeapp.generated.resources.Res
import ramadhancamp.composeapp.generated.resources.compose_multiplatform
import kotlin.collections.listOf

@Composable
fun App(
    startScreen: StartScreen
) {
    MaterialTheme {
        val resultRepository: ResultNavigationRepository = koinInject()
        val navController = rememberAppNavigationController(
            startDestination = startScreen.toRootDestination(),
            resultRepository = resultRepository
        )
      /*  val bottomSheetStrategy = remember { BottomSheetSceneStrategy<NavKey>(navController) }*/

        val koin = getKoin()
        LaunchedEffect(navController) {
            koin.declare(navController)
        }

        NavDisplay(
            backStack = navController.rootBackStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            transitionSpec = { slideInHorizontally { it } togetherWith slideOutHorizontally { -it } },
            popTransitionSpec = { slideInHorizontally { -it } togetherWith slideOutHorizontally { it } },
            entryProvider = entryProvider {
                entry<RootDestination.Auth> { /*AuthScreen()*/ }
                entry<RootDestination.Main> { /*MainScreen()*/ }
            }
        )
    }
}