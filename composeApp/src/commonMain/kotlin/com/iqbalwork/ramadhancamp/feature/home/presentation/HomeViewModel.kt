package com.iqbalwork.ramadhancamp.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationControllerHolder
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
    private val navHolder: AppNavigationControllerHolder,
) : ViewModel() {

    companion object {
        const val RESULT_KEY = "home_result"
    }

    private val _lastResult = MutableStateFlow<String?>(null)
    val lastResult: StateFlow<String?> = _lastResult.asStateFlow()

    init {
        viewModelScope.launch {
            navHolder.get().subscribeToResult(RESULT_KEY).collect { result ->
                _lastResult.value = when (result) {
                    is NavigationResult.Success -> "✓ ${(result.value as? TextResult)?.text}"
                    is NavigationResult.Cancel  -> "✗ Cancelled"
                }
            }
        }
    }

    // ─── HomeMain ────────────────────────────────────────────────────────────
    fun navigateToDetail()      = navHolder.get().navigateToInsideTab(TabDestination.HomeDetail)
    fun replaceWithDetail()     = navHolder.get().navigateToInsideTab(TabDestination.HomeDetail, withReplace = true)
    fun navigateToAuth()        = navHolder.get().navigateTo(RootDestination.Auth)
    fun navigateToAuthReplace() = navHolder.get().navigateTo(RootDestination.Auth, withReplace = true)
    fun startNewFlowToAuth()    = navHolder.get().startNewFlow(RootDestination.Auth)
    fun switchToPray()          = navHolder.get().switchTab(AppTab.Pray)
    fun showHomeSheet()         = navHolder.get().showDialog(DialogDestination.HomeSheet)

    // ─── HomeDetail ──────────────────────────────────────────────────────────
    fun navigateToSubDetail() = navHolder.get().navigateToInsideTab(TabDestination.HomeSubDetail)
    fun back()                = navHolder.get().back()
    fun backWithResult()      = navHolder.get().back(
        NavigationResult.Success(RESULT_KEY, TextResult("From HomeDetail"))
    )

    // ─── HomeSubDetail ───────────────────────────────────────────────────────
    fun backToMain() = navHolder.get().backToScreen(TabDestination.HomeMain)
    fun backToMainWithResult() = navHolder.get().backToScreen(
        key = TabDestination.HomeMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From HomeSubDetail")),
    )
}
