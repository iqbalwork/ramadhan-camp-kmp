package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.RootDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TextResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navController: AppNavigationController,
) : ViewModel() {

    companion object {
        const val RESULT_KEY = "home_result"
    }

    private val _lastResult = MutableStateFlow<String?>(null)
    val lastResult: StateFlow<String?> = _lastResult.asStateFlow()

    init {
        viewModelScope.launch {
            navController.subscribeToResult(RESULT_KEY).collect { result ->
                _lastResult.value = when (result) {
                    is NavigationResult.Success -> "✓ ${(result.value as? TextResult)?.text}"
                    is NavigationResult.Cancel  -> "✗ Cancelled"
                }
            }
        }
    }

    // ─── HomeMain ────────────────────────────────────────────────────────────
    fun navigateToDetail()      = navController.navigateToInsideTab(TabDestination.HomeDetail)
    fun replaceWithDetail()     = navController.navigateToInsideTab(TabDestination.HomeDetail, withReplace = true)
    fun navigateToAuth()        = navController.navigateTo(RootDestination.Auth)
    fun navigateToAuthReplace() = navController.navigateTo(RootDestination.Auth, withReplace = true)
    fun startNewFlowToAuth()    = navController.startNewFlow(RootDestination.Auth)
    fun switchToPray()          = navController.switchTab(AppTab.Pray)
    fun showHomeSheet()         = navController.showDialog(DialogDestination.HomeSheet)

    // ─── HomeDetail ──────────────────────────────────────────────────────────
    fun navigateToSubDetail() = navController.navigateToInsideTab(TabDestination.HomeSubDetail)
    fun back()                = navController.back()
    fun backWithResult()      = navController.back(
        NavigationResult.Success(RESULT_KEY, TextResult("From HomeDetail"))
    )

    // ─── HomeSubDetail ───────────────────────────────────────────────────────
    fun backToMain() = navController.backToScreen(TabDestination.HomeMain)
    fun backToMainWithResult() = navController.backToScreen(
        key = TabDestination.HomeMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From HomeSubDetail")),
    )
}
