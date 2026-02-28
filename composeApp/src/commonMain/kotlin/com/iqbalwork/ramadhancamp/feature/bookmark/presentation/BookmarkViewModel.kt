package com.iqbalwork.ramadhancamp.feature.bookmark.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppNavigationController
import com.iqbalwork.ramadhancamp.shared.common.navigation.AppTab
import com.iqbalwork.ramadhancamp.shared.common.navigation.DialogDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.navigation.TextResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarkViewModel(
    private val navController: AppNavigationController,
) : ViewModel() {

    companion object { const val RESULT_KEY = "bookmark_result" }

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

    fun navigateToDetail()  = navController.navigateToInsideTab(TabDestination.BookmarkDetail)
    fun replaceWithDetail() = navController.navigateToInsideTab(TabDestination.BookmarkDetail, withReplace = true)
    fun switchToHome()      = navController.switchTab(AppTab.Home)
    fun showBookmarkSheet() = navController.showDialog(DialogDestination.BookmarkSheet)

    fun navigateToSubDetail() = navController.navigateToInsideTab(TabDestination.BookmarkSubDetail)
    fun back()                = navController.back()
    fun backWithResult()      = navController.back(
        NavigationResult.Success(RESULT_KEY, TextResult("From BookmarkDetail"))
    )

    fun backToMain() = navController.backToScreen(TabDestination.BookmarkMain)
    fun backToMainWithResult() = navController.backToScreen(
        key = TabDestination.BookmarkMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From BookmarkSubDetail")),
    )
}
