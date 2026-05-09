package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.quran.domain.repository.QuranRepository
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResult
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationResultData
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuranMainViewModel(
    navigationManager: NavigationManager,
    private val quranRepository: QuranRepository
) : BaseViewModel<Unit, QuranMainState, QuranMainEvent, QuranMainEffect>(
    Unit, QuranMainState(), navigationManager,
    resultKeys = arrayOf("focus_search")
) {

    private var searchJob: Job? = null

    init {
        loadSurahs()
    }

    override fun navigationResultSuccess(key: String, data: NavigationResultData?) {
        super.navigationResultSuccess(key, data)
        if (key == "focus_search") {
            updateState { copy(focusSearch = true) }
        }
    }

    private fun loadSurahs() {
        updateState { copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            quranRepository.getSurahs()
                .onSuccess { surahs ->
                    updateState { copy(isLoading = false, surahs = surahs) }
                }
                .onFailure {
                    updateState { copy(isLoading = false, isError = true) }
                }
        }
    }

    override fun handleEvent(event: QuranMainEvent) {
        when (event) {
            is QuranMainEvent.Search -> {
                updateState { copy(searchQuery = event.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500) // debounce
                    if (event.query.isBlank()) {
                        updateState { copy(searchResults = null, isLoading = false) }
                        loadSurahs()
                    } else {
                        updateState { copy(isLoading = true, isError = false) }
                        quranRepository.search(event.query)
                            .onSuccess { results ->
                                updateState { copy(isLoading = false, searchResults = results) }
                            }
                            .onFailure {
                                updateState { copy(isLoading = false, isError = true) }
                            }
                    }
                }
            }
            is QuranMainEvent.SurahClicked -> {
                navigationManager.navigateToInsideTab(
                    TabDestination.QuranDetail(QuranDetailScreenParameters(event.surahId))
                )
            }
            is QuranMainEvent.AyatClicked -> {
                navigationManager.navigateToInsideTab(
                    TabDestination.QuranDetail(
                        QuranDetailScreenParameters(
                            surahId = event.surahNumber,
                            scrollToAyat = event.ayatNumber
                        )
                    )
                )
            }
            is QuranMainEvent.FocusSearchConsumed -> {
                updateState { copy(focusSearch = false) }
            }
        }
    }
}
