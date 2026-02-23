package com.iqbalwork.ramadhancamp.feature.quran.presentation

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

class QuranViewModel(
    private val navHolder: AppNavigationControllerHolder,
) : ViewModel() {

    companion object { const val RESULT_KEY = "quran_result" }

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

    fun navigateToDetail()  = navHolder.get().navigateToInsideTab(TabDestination.QuranDetail)
    fun replaceWithDetail() = navHolder.get().navigateToInsideTab(TabDestination.QuranDetail, withReplace = true)
    fun switchToQibla()     = navHolder.get().switchTab(AppTab.Qibla)
    fun showQuranSheet()    = navHolder.get().showDialog(DialogDestination.QuranSheet)

    fun navigateToSubDetail() = navHolder.get().navigateToInsideTab(TabDestination.QuranSubDetail)
    fun back()                = navHolder.get().back()
    fun backWithResult()      = navHolder.get().back(
        NavigationResult.Success(RESULT_KEY, TextResult("From QuranDetail"))
    )

    fun backToMain() = navHolder.get().backToScreen(TabDestination.QuranMain)
    fun backToMainWithResult() = navHolder.get().backToScreen(
        key = TabDestination.QuranMain,
        navigationResult = NavigationResult.Success(RESULT_KEY, TextResult("From QuranSubDetail")),
    )
}
