package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.lifecycle.viewModelScope
import com.iqbalwork.ramadhancamp.feature.quran.domain.repository.QuranRepository
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainEffect
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainState
import com.iqbalwork.ramadhancamp.shared.common.navigation.NavigationManager
import com.iqbalwork.ramadhancamp.shared.common.navigation.TabDestination
import com.iqbalwork.ramadhancamp.shared.common.ui.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuranMainViewModel(
    navigationManager: NavigationManager,
    private val quranRepository: QuranRepository
) : BaseViewModel<Unit, QuranMainState, QuranMainEvent, QuranMainEffect>(Unit, QuranMainState(), navigationManager) {

    private var searchJob: Job? = null

    init {
        loadSurahs()
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
                        loadSurahs()
                    } else {
                        updateState { copy(isLoading = true, isError = false) }
                        quranRepository.searchSurah(event.query)
                            .onSuccess { surahs ->
                                updateState { copy(isLoading = false, surahs = surahs) }
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
        }
    }
}
