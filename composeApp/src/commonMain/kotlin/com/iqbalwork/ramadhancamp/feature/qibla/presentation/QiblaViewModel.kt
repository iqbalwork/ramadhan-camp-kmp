package com.iqbalwork.ramadhancamp.feature.qibla.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationControllerHolder
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TextResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QiblaViewModel(
    private val navHolder: AppNavigationControllerHolder,
) : ViewModel() {

    companion object { const val RESULT_KEY = "qibla_result" }

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

    fun navigateToDetail()  = navHolder.get().navigateToInsideTab(TabDestination.QiblaDetail)
    fun replaceWithDetail() = navHolder.get().navigateToInsideTab(TabDestination.QiblaDetail, withReplace = true)
    fun switchToBookmark()  = navHolder.get().switchTab(AppTab.Bookmark)
    fun showQiblaSheet()    = navHolder.get().showDialog(DialogDestination.QiblaSheet)

    fun navigateToSubDetail() = navHolder.get().navigateToInsideTab(TabDestination.QiblaSubDetail)
    fun back()                = navHolder.get().back()
    fun backWithResult()      = navHolder.get().back(
        NavigationResult.Success(RESULT_KEY, TextResult("From QiblaDetail"))
    )

    fun backToMain() = navHolder.get().backToScreen(TabDestination.QiblaMain)
    fun backToMainWithResult() = navHolder.get().backToScreen(
        key = TabDestination.QiblaMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From QiblaSubDetail")),
    )
}
