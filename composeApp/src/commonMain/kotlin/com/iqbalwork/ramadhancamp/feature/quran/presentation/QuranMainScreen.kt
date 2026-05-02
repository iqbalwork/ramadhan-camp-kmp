package com.iqbalwork.ramadhancamp.feature.quran.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.QuranSearchBar
import com.iqbalwork.ramadhancamp.feature.quran.presentation.components.SurahListItem
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainEvent
import com.iqbalwork.ramadhancamp.feature.quran.presentation.model.QuranMainState
import com.iqbalwork.ramadhancamp.shared.common.extension.rememberViewModel
import com.iqbalwork.ramadhancamp.shared.common.ui.rememberDispatch
import com.iqbalwork.ramadhancamp.shared.common.ui.theme.RamadhanTheme

@Composable
fun QuranMainScreen() {
    val viewModel: QuranMainViewModel = rememberViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    QuranMainContent(state = state, action = viewModel.rememberDispatch())
}

@Composable
private fun QuranMainContent(
    state: QuranMainState,
    action: (QuranMainEvent) -> Unit
) {
    Scaffold(
        containerColor = RamadhanTheme.colors.bgPrimary,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets(bottom = innerPadding.calculateBottomPadding()))
        ) {
            Column(
                modifier = Modifier
                    .background(RamadhanTheme.colors.bgPrimary)
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                Text(
                    text = "Al-Quran",
                    style = RamadhanTheme.typography.headlineLarge,
                    color = RamadhanTheme.colors.textPrimary,
                    modifier = Modifier.padding(16.dp)
                )
                QuranSearchBar(
                    query = state.searchQuery,
                    onQueryChange = { action(QuranMainEvent.Search(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.surahs, key = { it.number }) { surah ->
                    SurahListItem(
                        surah = surah,
                        onClick = { action(QuranMainEvent.SurahClicked(surah.number)) }
                    )
                    HorizontalDivider(color = RamadhanTheme.colors.divider, thickness = 1.dp)
                }
            }
        }
    }
}

